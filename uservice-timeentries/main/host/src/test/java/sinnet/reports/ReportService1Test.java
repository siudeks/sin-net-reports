package sinnet.reports;

import java.io.ByteArrayInputStream;
import java.util.zip.ZipInputStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.vavr.collection.Iterator;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import lombok.val;
import sinnet.AppOperations;
import sinnet.Profiles;
import sinnet.db.PostgresDbExtension;
import sinnet.host.HostTestContextConfiguration;
import sinnet.report1.grpc.ActivityDetails;
import sinnet.report1.grpc.CustomerDetails;
import sinnet.report1.grpc.ReportRequest;
import sinnet.report1.grpc.ReportRequests;
import sinnet.reports.grpc.Date;

@SpringBootTest
@ContextConfiguration(classes = { HostTestContextConfiguration.class })
@ActiveProfiles(Profiles.TEST)
@ExtendWith(PostgresDbExtension.class)
class ReportService1Test {

  @Autowired
  AppOperations operations;

  @Test
  void produceReportWithMinDataCase1() {
    var self = operations.getSelfReport();
    var customer = CustomerDetails.newBuilder().build();
    var activity = ActivityDetails.newBuilder().build();
    var request = ReportRequest.newBuilder().build();
    var res = self.produce(request);
    var data = res.getData().toByteArray();

    Assertions.assertThat(data).isNotEmpty();
  }

  @Test
  @SneakyThrows
  void produceReportWithMinDataCase2() {
    var self = operations.getSelfReport();
    var customer = CustomerDetails.newBuilder()
        .setCustomerName("Customer name")
        .setCustomerId("Customer ID")
        .setCustomerAddress("Customer Address")
        .setCustomerCity("Customer City")
        .build();
    var who = "PERSON WITH LONG NAME 1 LINE";
    var when = Date.newBuilder().setYear(2001).setMonth(2).setDayOfTheMonth(3).build();
    var request = ReportRequest.newBuilder()
        .setCustomer(customer)
        .addDetails(ActivityDetails
            .newBuilder()
            .setWho(who)
            .setWhen(when)
            .setDescription("Position 1")
            .setHowFarInKms(23)
            .build())
        .addDetails(ActivityDetails
            .newBuilder()
            .setWho(who)
            .setWhen(when)
            .setDescription("Position 2")
            .setHowLongInMins(12)
            .setHowFarInKms(34)
            .build())
        .build();
    var res = self.produce(request);
    var data = res.getData().toByteArray();

    // produce local copy of the file for manual review
    // Files.write(Paths.get("temp_raport1_from_test.pdf"), data);

    Assertions.assertThat(data).isNotEmpty();
  }

  @Test
  @SneakyThrows
  void producePackEndpoint() {
    var self = operations.getSelfReport();
    val request = ReportRequest.newBuilder().build();
    val pack = ReportRequests.newBuilder()
        .addItems(request)
        .addItems(request)
        .build();
    val res = self.producePack(pack);

    var data = res.getData().toByteArray();
    val byteStream = new ByteArrayInputStream(data);
    val zis = new ZipInputStream(byteStream);

    var entry1 = zis.getNextEntry();
    var entry2 = zis.getNextEntry();
    var entry3 = zis.getNextEntry();
    Assertions.assertThat(entry1.getSize()).isNotZero();
    Assertions.assertThat(entry2.getSize()).isNotZero();
    Assertions.assertThat(entry3).isNull();
  }

  /**
   * https://github.com/onlexnet/sin-net-reports/issues/73
   * Three digits for element number
   */
  @Test
  void files_should_be_numerated_using_three_sigits() {
    var self = operations.getSelfReport();
    val request = ReportRequest.newBuilder().build();
    val pack = Stream.from(1).take(3)
        .foldLeft(ReportRequests.newBuilder(), (acc, v) -> {
          val item = ReportRequest.newBuilder();
          return acc.addItems(item);
        })
        .build();
    val res = self.producePack(pack);

    var data = res.getData().toByteArray();
    val byteStream = new ByteArrayInputStream(data);
    val zis = new ZipInputStream(byteStream);

    val fileNames = Iterator
        .iterate(() -> Try.of(() -> zis.getNextEntry()).map(Option::of).get())
        .map(it -> {
          return it;
        })
        .map(it -> it.getName())
        .toJavaList();

    Assertions.assertThat(fileNames).containsExactly("001-.pdf", "002-.pdf", "003-.pdf");
  }

  /**
   * https://github.com/onlexnet/sin-net-reports/issues/73
   * Slash in client name produces valid filename
   */
  @Test
  @SneakyThrows
  void files_should_be_normalized_when_customer_has_special_characters_in_name() {
    var self = operations.getSelfReport();
    var companyWithSlashInName = ReportRequest
        .newBuilder()
        .setCustomer(CustomerDetails.newBuilder().setCustomerName("My/Company"))
        .build();
    val request = ReportRequests.newBuilder()
        .addItems(companyWithSlashInName)
        .build();
    val res = self.producePack(request);

    var data = res.getData().toByteArray();
    val byteStream = new ByteArrayInputStream(data);
    val zis = new ZipInputStream(byteStream);

    var entry1 = zis.getNextEntry();
    Assertions.assertThat(entry1.getName()).isEqualTo("001-My_Company.pdf");
  }

  // // /** https://github.com/onlexnet/sin-net-reports/issues/60 */
  // // @Test
  // // class
}
