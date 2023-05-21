package sinnet.bdd;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.spring.CucumberContextConfiguration;
import sinnet.Profiles;
import sinnet.Program;

@CucumberContextConfiguration
@ActiveProfiles({Profiles.App.TEST, Profiles.Jwt.Local})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "DAPR_GRPC_PORT=0" }, classes = { Program.class, PortsConfigurer.class })
public class CucumberContextConfigurer {
}
