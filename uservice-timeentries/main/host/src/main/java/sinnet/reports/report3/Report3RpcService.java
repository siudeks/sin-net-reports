package sinnet.reports.report3;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;

import lombok.SneakyThrows;
import sinnet.grpc.projects.RpcQueryHandler;
import sinnet.report3.grpc.ReportRequest;
import sinnet.reports.grpc.FileResponse;

/**
 * Exposes gRPC endpoints to allow produce PDF report based on requested data.
 */
@Service
final class Report3RpcService implements RpcQueryHandler<ReportRequest, FileResponse> {

  @Override
  @SneakyThrows
  public FileResponse apply(ReportRequest request) {
    // TODO close resource on exit
    var baos = new ByteArrayOutputStream();
    var model = ReportResults.apply(DtoMapper.fromDto(request));
    var report = model.content();
    baos.write(report);
    baos.close();

    var binaryData = baos.toByteArray();
    var dtoData = ByteString.copyFrom(binaryData);
    return FileResponse.newBuilder()
      .setFileName("raport-3.pdf")
      .setData(dtoData)
      .build();
  }
}
