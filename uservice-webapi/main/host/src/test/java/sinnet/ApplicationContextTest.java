package sinnet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({Profiles.App.TEST, Profiles.Jwt.Local})
class ApplicationContextTest {

  @Test
  void initializeContext() {
  }

}
