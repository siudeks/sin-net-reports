package sinnet;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import sinnet.db.PostgresDbExtension;
import sinnet.host.HostTestContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { HostTestContextConfiguration.class })
@ActiveProfiles(Profiles.TEST)
@ExtendWith(PostgresDbExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ShouldValidateTest {

  final int maximumSizeOfUserEmail = 50;

  @Autowired
  AppOperations operations;
  
  @Test
  void too_long_name() {
    var tooLongEmail = RandomStringUtils.randomAlphanumeric(maximumSizeOfUserEmail + 1);
    Assertions
        .assertThatCode(() -> operations.create(tooLongEmail))
        .isInstanceOfSatisfying(StatusRuntimeException.class,
          ex -> Assertions.assertThat(ex.getStatus().getCode()).isEqualTo(Status.FAILED_PRECONDITION.getCode()));
  }

  @Test
  void maximum_name() {
    var longEmail = RandomStringUtils.randomAlphanumeric(maximumSizeOfUserEmail);
    Assertions
        .assertThatCode(() -> operations.create(longEmail))
        .doesNotThrowAnyException();
  }
}
