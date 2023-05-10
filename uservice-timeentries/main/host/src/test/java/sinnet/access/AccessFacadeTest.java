package sinnet.access;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.grpc.StatusRuntimeException;
import sinnet.db.PostgresDbExtension;
import sinnet.Profiles;
import sinnet.grpc.projects.generated.UserToken;
import sinnet.host.HostTestContextConfiguration;
import sinnet.domain.access.AccessFacade;
import sinnet.domain.model.ValProjectId;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { HostTestContextConfiguration.class })
@ActiveProfiles(Profiles.TEST)
@ExtendWith(PostgresDbExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AccessFacadeTest {

  @Autowired
  AccessFacade accessFacade;

  @Test
  void should_reject_invalid_owner() {
    var requestor = UserToken.newBuilder()
        .setRequestorEmail("my@email")
        .build();
    var eid = ValProjectId.of(UUID.randomUUID());

    Assertions
      .assertThatCode(() -> accessFacade.guardAccess(requestor, eid, a -> a::canDeleteProject))
      .isExactlyInstanceOf(StatusRuntimeException.class)
      .hasMessageContaining("PERMISSION_DENIED");
  }
}
