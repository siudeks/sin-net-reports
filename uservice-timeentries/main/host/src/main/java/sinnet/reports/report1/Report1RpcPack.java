package sinnet.reports.report1;

// import scala.collection.JavaConversions._

// import scala.language.implicitConversions
// import javax.inject.Singleton;
// import io.grpc.stub.StreamObserver
import sinnet.report1.grpc.ReportRequest;
import sinnet.report1.grpc.ReportRequests;
import sinnet.reports.grpc.Response;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;

import io.vavr.collection.Iterator;
import io.vavr.collection.Stream;
import lombok.SneakyThrows;

import java.util.zip.ZipEntry;

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
import java.util.zip.ZipOutputStream;

// import scala.collection.JavaConverters._
// import java.util.UUID
// import java.util.zip.ZipEntry
// import com.lowagie.text.pdf.PdfTable
// import java.time.format.DateTimeFormatter
// import io.quarkus.grpc.GrpcService

import sinnet.grpc.projects.RpcQueryHandler;

/**
 * Exposes gRPC endpoints to allow produce PDF report based on requested data.
 */
@Service
final class Report1RpcPack implements RpcQueryHandler<ReportRequests, Response> {

  @Override
  @SneakyThrows
  public Response apply(ReportRequests request) {
    try (
        var baos = new ByteArrayOutputStream();
        var zos = new ZipOutputStream(baos)) {

      Iterator
          .ofAll(request.getItemsList())
          .zip(Stream.rangeClosed(1, Integer.MAX_VALUE))
          .forEach(it -> {
            var itemDto = it._1;
            var index = it._2;
            var item = Mapper.fromDto(itemDto);
            var model = new ReportResults().apply(item);
            var report = model.content();
            var indexWithLeadingDigits = String.format("%03d", Integer.valueOf(index));
            var fileName = indexWithLeadingDigits + "-" + item.customer().customerName() + ".pdf";
            var normalizedFileName = fileName.replace("/", "_");
            var entry = new ZipEntry(normalizedFileName);
            writeEntry(zos, entry, report);
          });
  
      zos.close();

      var binaryData = baos.toByteArray();
      var dtoData = ByteString.copyFrom(binaryData);
      return Response
          .newBuilder()
          .setData(dtoData)
          .build();
    }
  }

  @SneakyThrows
  static void writeEntry(ZipOutputStream stream, ZipEntry entry, byte[] data) {
    stream.putNextEntry(entry);
    stream.write(data);
    stream.closeEntry();
  }
}
