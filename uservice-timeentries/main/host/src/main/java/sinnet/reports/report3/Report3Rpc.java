package sinnet.reports.report3;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import sinnet.report3.grpc.ReportRequest;
import sinnet.report3.grpc.ReportsGrpc.ReportsImplBase;
import sinnet.reports.grpc.FileResponse;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
class Report3Rpc extends ReportsImplBase {
  private final Report3RpcService service;

  @Override
  public void produce(ReportRequest request, StreamObserver<FileResponse> responseObserver) {
    service.query(request, responseObserver);
  }
}
