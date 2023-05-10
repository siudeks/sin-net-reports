package sinnet.reports.report2;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;

import lombok.SneakyThrows;
import lombok.val;
import sinnet.grpc.projects.RpcQueryHandler;
import sinnet.report2.grpc.ReportRequest;
import sinnet.reports.grpc.Response;

/**
 * Exposes gRPC endpoints to allow produce PDF report based on requested data.
 */
@Service
final class Report2RpcPack implements RpcQueryHandler<ReportRequest, Response> {

  @Override
  @SneakyThrows
  public Response apply(ReportRequest request) {
    // TODO close resource on exit
    val baos = new ByteArrayOutputStream();
    val model = ReportResults.apply(DtoMapper.fromDto(request));
    val report = model.content();
    baos.write(report);
    baos.close();

    val binaryData = baos.toByteArray();
    val dtoData = ByteString.copyFrom(binaryData);
    return Response.newBuilder()
        .setData(dtoData)
        .build();
  }
}
