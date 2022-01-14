package sinnet.web;

import static sinnet.reports.grpc.YearMonth.newBuilder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.Array;
import io.vavr.collection.HashMap;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.control.Option;
import sinnet.FutureExecutor;
import sinnet.models.ActionDuration;
import sinnet.models.Distance;
import sinnet.models.Name;
import sinnet.read.ActionProjector;
import sinnet.read.CustomerProjection;
import sinnet.report3.grpc.CustomerDetails;
import sinnet.report3.grpc.GroupDetails;
import sinnet.report3.grpc.ReportRequest;
import sinnet.report3.grpc.ReportsGrpc;

@RestController
@RequestMapping(path = "/api/raporty/3")
class Report3Controller implements ActionProjector {

  @Autowired
  private CustomerProjection projection;

  @Autowired
  private ReportsGrpc.ReportsFutureStub reportsClient;

  @Autowired
  private FutureExecutor executor;

  @RequestMapping(value = "/{projectId}", method = RequestMethod.GET, produces = "application/pdf")
  public CompletionStage<ResponseEntity<byte[]>> downloadPdfFile(@PathVariable UUID projectId) {

    var headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Content-Disposition", "inline; filename=report-3.pdf");
    headers.add("Expires", "0");

    return projection
        .list(projectId)
        .toCompletionStage()
        .thenCompose(it -> {
          var reportRequest = asReportRequest(it);
          var reportResult = reportsClient.produce(reportRequest);
          return executor.asFuture(reportResult, Function1.identity());
        })
        .thenApplyAsync(it -> {
          var result = it.getData().toByteArray();
          return ResponseEntity.ok()
              .headers(headers)
              .contentLength(result.length)
              .contentType(MediaType.parseMediaType("application/octet-stream"))
              .body(result);
        });
  }


  ReportRequest asReportRequest(List<CustomerProjection.CustomerModel> items) {
    return items
      .filter(it -> StringUtils.isNotBlank(it.getValue().getOperatorEmail()))
      .map(it -> Tuple.of(
        it.getValue().getOperatorEmail(),
        CustomerDetails.newBuilder()
          .setName(it.getValue().getCustomerName().getValue())
          .setAddress(it.getValue().getCustomerAddress())
          .setCity(it.getValue().getCustomerCityName().getValue())
          .build()))
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
