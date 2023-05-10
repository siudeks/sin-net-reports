package sinnet

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import io.quarkus.example.GreeterGrpc;
import io.quarkus.example.HelloRequest;
import javax.inject.Inject
import scala.annotation.meta.field
import sinnet.reports.grpc.{YearMonth => YearMonthDTO}
import sinnet.report3.grpc.{
    ReportRequest => ReportRequestDTO,
    CustomerDetails => CustomerDetailsDTO,
    GroupDetails => GroupDetailsDTO,
    ReportsGrpc}
import org.assertj.core.api.Assertions
import java.util.zip.ZipInputStream
import java.io.ByteArrayInputStream
import io.quarkus.grpc.GrpcClient
import org.junit.jupiter.api.Timeout

@QuarkusTest
@Timeout(3)
class ReportService3Test {
    
    @Inject
    @GrpcClient
    var self: ReportsGrpc.ReportsBlockingStub = _

    @Test
    def produceReportWithEmptyValues(): Unit = {
        val request = ReportRequestDTO.newBuilder()
            .addDetails(GroupDetailsDTO
                .newBuilder()
                .addDetails(CustomerDetailsDTO
                    .newBuilder()))
            .build()

        val res = self.produce(request)
    }

    @Test
    def produceReportWithMinDataCase2(): Unit = {
        val request = ReportRequestDTO.newBuilder()
            .addDetails(GroupDetailsDTO
                .newBuilder()
                .setPersonName("PersonA")
                .addDetails(CustomerDetailsDTO
                    .newBuilder
                    .setName("Customer1")
                    .setAddress("Address1")
                    .setCity("City1")))
            .build()

        val res = self.produce(request)
        var data = res.getData().toByteArray()
        
        // uncomment block of lines below to produce a local example raport file
        import java.io.File
        import java.nio.file.Files
        import java.nio.file.Paths
        Files.write(Paths.get("temp_raport3_from_test.pdf"), data)

        Assertions.assertThat(data).isNotEmpty()
    }
}
