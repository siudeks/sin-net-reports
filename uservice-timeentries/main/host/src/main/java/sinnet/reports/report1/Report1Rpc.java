package sinnet.reports.report1;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import sinnet.report1.grpc.ReportRequest;
import sinnet.report1.grpc.ReportRequests;
import sinnet.report1.grpc.ReportsGrpc.ReportsImplBase;
import sinnet.reports.grpc.Response;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
class Report1Rpc extends ReportsImplBase {
  private final Report1RpcService service;
  private final Report1RpcPack servicePack;

  @Override
  public void produce(ReportRequest request, StreamObserver<Response> responseObserver) {
    service.query(request, responseObserver);
  }

  @Override
  public void producePack(ReportRequests request, StreamObserver<Response> responseObserver) {
    servicePack.query(request, responseObserver);
  }

}
