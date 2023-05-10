package sinnet.reports;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import lombok.SneakyThrows;
import sinnet.AppOperations;
import sinnet.db.PostgresDbExtension;
import sinnet.host.HostTestContextConfiguration;
import sinnet.report2.grpc.ActivityDetails;
import sinnet.report2.grpc.ReportRequest;
import sinnet.reports.grpc.YearMonth;
import sinnet.Profiles;

@SpringBootTest
@ContextConfiguration(classes = { HostTestContextConfiguration.class })
@ActiveProfiles(Profiles.TEST)
@ExtendWith(PostgresDbExtension.class)
@Timeout(value = 3)
class ReportService2Test {

  @Autowired
  AppOperations operations;

  @Test
  void produceReportWithMinDataCase1() {
    var activity = ActivityDetails.newBuilder().build();
    var request = ReportRequest.newBuilder().build();
    var self = operations.getSelfReport2();
    var res = self.produce(request);
    var data = res.getData().toByteArray();
    Assertions
        .assertThat(data)
        .isNotEmpty();
  }

  @Test
  @SneakyThrows
  void produceReportWithMinDataCase2() {
    var self = operations.getSelfReport2();
    var period = YearMonth.newBuilder().setYear(2001).setMonth(1);
    var request = ReportRequest.newBuilder()
        .addDetails(ActivityDetails
            .newBuilder()
            .setPersonName("Ala")
            .setYearMonth(period)
            .setHowLongInMins(42)
            .build())
        .addDetails(ActivityDetails
            .newBuilder()
            .setPersonName("Ola")
            .setYearMonth(period)
            .setHowLongInMins(123)
            .setHowFarInKms(4)
            .build())
        .build();
    var res = self.produce(request);
    var data = res.getData().toByteArray();

    Files.write(Paths.get("temp_raport2_from_test.pdf"), data);

    Assertions.assertThat(data).isNotEmpty();
  }

}
