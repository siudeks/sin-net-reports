package sinnet.reports.report2;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import sinnet.report2.grpc.ReportRequest;
import sinnet.report2.grpc.ReportsGrpc.ReportsImplBase;
import sinnet.reports.grpc.Response;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
class Report2Rpc extends ReportsImplBase {
  private final Report2RpcPack servicePack;

  @Override
  public void produce(ReportRequest request, StreamObserver<Response> responseObserver) {
    servicePack.query(request, responseObserver);
  }
}
