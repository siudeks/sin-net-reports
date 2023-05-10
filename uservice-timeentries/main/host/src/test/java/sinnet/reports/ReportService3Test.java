package sinnet.reports;

import sinnet.reports.grpc.YearMonth;
import sinnet.report3.grpc.ReportRequest;

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
import sinnet.Profiles;
import sinnet.db.PostgresDbExtension;
import sinnet.host.HostTestContextConfiguration;
import sinnet.report3.grpc.CustomerDetails;
import sinnet.report3.grpc.GroupDetails;
import sinnet.report3.grpc.ReportsGrpc;

@SpringBootTest
@ContextConfiguration(classes = { HostTestContextConfiguration.class })
@ActiveProfiles(Profiles.TEST)
@ExtendWith(PostgresDbExtension.class)
@Timeout(value = 3)
class ReportService3Test {

  @Autowired
  AppOperations operations;

  @Test
  void produceReportWithEmptyValues() {
    var self = operations.getSelfReport3();
    var request = ReportRequest.newBuilder()
        .addDetails(GroupDetails
            .newBuilder()
            .addDetails(CustomerDetails
                .newBuilder()))
        .build();

    var res = self.produce(request);
  }

  @Test
  @SneakyThrows
  void produceReportWithMinDataCase2() {
    var self = operations.getSelfReport3();
    var request = ReportRequest.newBuilder()
        .addDetails(GroupDetails
            .newBuilder()
            .setPersonName("PersonA")
            .addDetails(CustomerDetails
                .newBuilder()
                .setName("Customer1")
                .setAddress("Address1")
                .setCity("City1")))
        .build();

    var res = self.produce(request);
    var data = res.getData().toByteArray();

    Files.write(Paths.get("temp_raport3_from_test.pdf"), data);

    Assertions.assertThat(data).isNotEmpty();
  }
}
