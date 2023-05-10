package sinnet.ws;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import sinnet.gql.TimeentriesMapper;
import sinnet.gql.utils.PropsBuilder;
import sinnet.grpc.GrpcCustomers;
import sinnet.grpc.customers.ListReply;
import sinnet.grpc.customers.ListRequest;
import sinnet.report3.grpc.CustomerDetails;
import sinnet.report3.grpc.GroupDetails;
import sinnet.report3.grpc.ReportRequest;
import sinnet.report3.grpc.Reports;

@Path("/api/raporty")
public class Report3Controller implements TimeentriesMapper {

  @Inject GrpcCustomers customera;

  @GrpcClient("uservice-reports")
  Reports reportsClient;
  
  @GET
  @Path("/3/{projectId}")
  @Produces("application/pdf")
  public Uni<Response> downloadPdfFile(UUID projectId) {

    var projectIdAsString = projectId.toString();
    
    var listRequest = ListRequest.newBuilder()
        .setProjectId(projectIdAsString)
        .build();
    return customera.list(listRequest)
        .flatMap(it -> {
          var reportRequest = asReportRequest(it);
          return reportsClient.produce(reportRequest);
        })
        .map(it -> {
          var result = it.getData().toByteArray();
          return Response.ok(result)
            .header("Cache-Control", "no-cache, no-store, must-revalidate")
            .header("Content-Disposition", "inline; filename=report-3.pdf")
            .header("Expires", "0")
            .header(HttpHeaders.CONTENT_LENGTH, result.length)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
            .build();
        });
  }

  // TODO move aggregation closer to data service
  ReportRequest asReportRequest(ListReply reply) {
    return List.ofAll(reply.getCustomersList())
      .filter(it -> StringUtils.isNotBlank(it.getValue().getOperatorEmail()))
      .map(it -> Tuple.of(
          it.getValue().getOperatorEmail(),
          PropsBuilder.build(CustomerDetails.newBuilder())
            .set(b -> b::setName, it.getValue().getCustomerName())
            .set(b -> b::setAddress, it.getValue().getCustomerAddress())
            .set(b -> b::setCity, it.getValue().getCustomerCityName())
            .done().build()))
      .foldLeft(
        HashMap.<String, List<CustomerDetails>>empty(),
        (acc, v) -> acc.put(v._1, List.of(v._2), (o1, o2) -> o1.appendAll(o2)))
      .mapKeys(it -> GroupDetails.newBuilder().setPersonName(it))
      .toList()
      .map(it -> it._1.addAllDetails(it._2))
      .foldLeft(ReportRequest.newBuilder(), (acc, v) -> acc.addDetails(v))
      .build();
    }

}