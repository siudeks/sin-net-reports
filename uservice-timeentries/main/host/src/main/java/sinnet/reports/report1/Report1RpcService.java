package sinnet.reports.report1;

// import scala.collection.JavaConversions._

// import scala.language.implicitConversions
// import javax.inject.Singleton;
// import io.grpc.stub.StreamObserver
import sinnet.report1.grpc.ReportRequest;
import sinnet.report1.grpc.ReportRequests;
import sinnet.reports.grpc.Response;

import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;

// import scala.util.Try

// import java.time.LocalDate
// import resource._

// import com.lowagie.text.Document
// import com.lowagie.text.Font
// import com.lowagie.text.Paragraph
// import com.lowagie.text.alignment.HorizontalAlignment
// import com.lowagie.text.pdf.PdfPCell
// import com.lowagie.text.pdf.PdfPTable
// import com.lowagie.text.pdf.PdfWriter
// import java.io.ByteArrayOutputStream
// import com.google.protobuf.ByteString
// import io.grpc.Deadline
// import io.grpc.Context
// import java.util.zip.ZipOutputStream

// import scala.collection.JavaConverters._
// import java.util.UUID
// import java.util.zip.ZipEntry
// import com.lowagie.text.pdf.PdfTable
// import java.time.format.DateTimeFormatter
// import io.quarkus.grpc.GrpcService

import sinnet.grpc.projects.RpcQueryHandler;

/** Exposes gRPC endpoints to allow produce PDF report based on requested data. */
@Service
final class Report1RpcService implements RpcQueryHandler<ReportRequest, Response> {

  @Override
  public Response apply(ReportRequest request) {
    var requestModel = Mapper.fromDto(request);
    var model = new ReportResults().apply(requestModel);
    var binaryData = model.content();
    var dtoData = ByteString.copyFrom(binaryData);
    var response = Response
        .newBuilder()
        .setData(dtoData)
        .build();
    return response;
  }
}

