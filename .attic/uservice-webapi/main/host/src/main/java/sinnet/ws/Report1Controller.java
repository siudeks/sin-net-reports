package sinnet.ws;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.Value;
import sinnet.gql.TimeentriesMapper;
import sinnet.grpc.GrpcCustomers;
import sinnet.grpc.GrpcTimeEntries;
import sinnet.report1.grpc.ActivityDetails;
import sinnet.report1.grpc.CustomerDetails;
import sinnet.report1.grpc.ReportRequest;
import sinnet.report1.grpc.ReportRequests;
import sinnet.report1.grpc.Reports;
import sinnet.reports.grpc.Date;

@Path("/api/raporty")
public class Report1Controller implements TimeentriesMapper {

  @Inject GrpcTimeEntries timeentries;

  @Inject GrpcCustomers customersClient;

  @GrpcClient("uservice-reports")
  Reports reportsClient;
  
  @GET
  @Path("/klienci/{projectId}/{year}/{month}")
  @Produces("application/pdf")
  public Uni<Response> downloadPdfFile(UUID projectId, int year, int month) {

    var dateFrom = LocalDate.of(year, month, 1);
    var dateTo = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
    var projectIdAsString = projectId.toString();
    return getTimeentries(projectIdAsString, dateFrom, dateTo)
      .flatMap(entries -> getCustomers(projectIdAsString).map(res -> Tuple.of(res, entries)))
      .flatMap(it -> {
        var entries = it._2;
        var customers = it._1;
        var reportRequest = asReportRequests(entries, customers);
        return reportsClient.producePack(reportRequest);
      })
      .map(it -> {
        var result = it.getData().toByteArray();
        return Response.ok(result)
          .header("Cache-Control", "no-cache, no-store, must-revalidate")
          .header("Content-Disposition", "inline; filename=report " + year + "-" + month + ".zip")
          .header("Expires", "0")
          .header(HttpHeaders.CONTENT_LENGTH, result.length)
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
          .build();
      });
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

  @Value
  class CustomerModel {
    String customerId;
    String customerName;
    String customerCity;
    String customerAddress;
  }

  private Uni<List<CustomerModel>> getCustomers(String projectId) {
    var request =  sinnet.grpc.customers.ListRequest.newBuilder()
      .setProjectId(projectId)
      .build();
    return customersClient.list(request)
      .map(items -> List.ofAll(items.getCustomersList().stream()))
      .map(items -> items.map(it -> new CustomerModel(it.getId().getEntityId(),
        it.getValue().getCustomerName(),
        it.getValue().getCustomerCityName(),
        it.getValue().getCustomerAddress())));
  }

  /**
   * Prepares raport request based on given items and give customer.
   * It does not check if the customer is actually same as defined in items.
   */
  Option<ReportRequest> asReportRequest(List<TimeEntryModel> items, Option<CustomerModel> maybeCustomer) {
    return items.headOption()
      .map(it -> {
        var requestBuilder = ReportRequest.newBuilder();

        if (maybeCustomer.isDefined()) {
          var customer = maybeCustomer.get();
          var customerId = customer.getCustomerId();
          var customerName = customer.getCustomerName();
          var customerCity = customer.getCustomerCity();
          var customerAddress = customer.getCustomerAddress();
          var customerBuilder = CustomerDetails.newBuilder()
              .setCustomerId(customerId.toString());
          Option.of(customerName).forEach(customerBuilder::setCustomerName);
          Option.of(customerCity).forEach(customerBuilder::setCustomerCity);
          Option.of(customerAddress).forEach(customerBuilder::setCustomerAddress);
          requestBuilder.setCustomer(customerBuilder);
        }

        items
            // let's simplify path of obtaining the value
            .forEach(item -> {
              var v = item;
              var builder = ActivityDetails.newBuilder();
              Option.of(v.getWhat()).forEach(builder::setDescription);
              builder
                  .setHowFarInKms(v.getHowFar())
                  .setHowLongInMins(v.getHowLong());
              Option.of(v.getWhen()).forEach(o -> builder
                  .setWhen(Date.newBuilder()
                  .setYear(v.getWhen().getYear())
                  .setMonth(v.getWhen().getMonthValue())
                  .setDayOfTheMonth(v.getWhen().getDayOfMonth())));
              // Option.of(v.getWho())
              //     .map(o -> o.split("@")[0])
              //     .forEach(builder::setWho);
              Option.of(item.getServicemanName())
                  .forEach(builder::setWho);

              requestBuilder.addDetails(builder);
            });
        
        return requestBuilder.build();
      });
  }

  ReportRequests asReportRequests(List<TimeEntryModel> items, List<CustomerModel> customers) {
    return items
        // lets group actions related to the same customer
        .groupBy(it -> it.getCustomerId())
        .map((k, v) -> {
          var customerId = k;
          var customer = customers.find(it -> Objects.equals(customerId, it.getCustomerId()));
          var newValue = asReportRequest(v, customer);
          return Tuple.of(k, newValue);
        })
        // key is no longer needed
        .map(kv -> kv._2)
        // unpack Option value to raw value
        .flatMap(Function1.identity())
        .foldLeft(ReportRequests.newBuilder(), (acc, v) -> acc.addItems(v))
        .build();
  }
}
