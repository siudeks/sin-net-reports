package sinnet.ws;

import static sinnet.reports.grpc.YearMonth.newBuilder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import lombok.Value;
import sinnet.gql.TimeentriesMapper;
import sinnet.grpc.GrpcTimeEntries;
import sinnet.report2.grpc.ReportRequest;
import sinnet.report2.grpc.Reports;

@Path("/api/raporty")
public class Report2Controller implements TimeentriesMapper {

  @Inject GrpcTimeEntries timeentries;

  @GrpcClient("uservice-reports")
  Reports reportsClient;
  
  @GET
  @Path("/2/{projectId}")
  @Produces("application/pdf")
  public Uni<Response> downloadPdfFile(UUID projectId,
      @QueryParam("yearFrom") int yearFrom, @QueryParam("monthFrom") int monthFrom,
      @QueryParam("yearTo") int yearTo, @QueryParam("monthTo") int monthTo) {

    var dateFrom = LocalDate.of(yearFrom, monthFrom, 1);
    var dateTo = LocalDate.of(yearTo, monthTo, 1).plusMonths(1).minusDays(1);
    var projectIdAsString = projectId.toString();


    return getTimeentries(projectIdAsString, dateFrom, dateTo)
      .flatMap(it -> {
        var entries = it;
        var reportRequest = asReportRequest(entries);
        return reportsClient.produce(reportRequest);
      })
      .map(it -> {
        var result = it.getData().toByteArray();
        return Response.ok(result)
          .header("Cache-Control", "no-cache, no-store, must-revalidate")
          .header("Content-Disposition", "inline; filename=report " + yearFrom + "-" + monthFrom + ".pdf")
          .header("Expires", "0")
          .header(HttpHeaders.CONTENT_LENGTH, result.length)
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
          .build();
      });
  }

  // TODO move such aggregation logic close to data service
  ReportRequest asReportRequest(List<TimeEntryModel> items) {
    var map = items
        .map(it -> Tuple.of(it.getServicemanName(),YearMonth.from(it.getWhen()), it.getHowLong(), it.getHowFar()))
        .map(it -> {
          var key = Tuple.of(it._1, it._2);
          return Tuple.of(key, Tuple.of(it._3, it._4));
        })
        .foldLeft(
          HashMap.<Tuple2<String, YearMonth>, Tuple2<Integer, Integer>>empty(),
          (acc, v) -> acc.put(v._1, v._2, (v1, v2) -> Tuple.of(v1._1 + v2._1, v1._2 + v2._2)));

    var requestBuilder = ReportRequest.newBuilder();
    map.forEach((k, v) -> requestBuilder.addDetailsBuilder()
        .setPersonName(k._1 + "")
        .setYearMonth(newBuilder().setYear(k._2.getYear()).setMonth(k._2.getMonthValue()))
        .setHowLongInMins(v._1)
        .setHowFarInKms(v._2)
        .build());
    return requestBuilder.build();
  }

  @Value 
  class TimeEntryModel {
    String customerId;
    String what;
    LocalDate when;
    String servicemanName;
    int howFar;
    int howLong;
  }

  private Uni<List<TimeEntryModel>> getTimeentries(String projectId, LocalDate from, LocalDate to) {
    var request = sinnet.grpc.timeentries.SearchQuery.newBuilder()
        .setProjectId(projectId)
        .setFrom(toGrpc(from))
        .setTo(toGrpc(to))
        .build();
    return timeentries.search(request)
      .map(items -> List.ofAll(items.getActivitiesList().stream()))
      .map(items -> items.map(it -> new TimeEntryModel(it.getCustomerId(),
        it.getDescription(),
        fromGrpc(it.getWhenProvided()),
        it.getServicemanName(),
        it.getDistance(),
        it.getDuration())));
  }

}
