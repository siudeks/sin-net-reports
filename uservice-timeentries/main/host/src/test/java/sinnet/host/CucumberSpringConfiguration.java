package sinnet.host;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import groovy.util.logging.Slf4j;
import io.cucumber.spring.CucumberContextConfiguration;
import sinnet.HostTestContextBootstrapper;

@CucumberContextConfiguration
// examples https://github.com/cucumber/cucumber-jvm/pull/1911
@BootstrapWith(HostTestContextBootstrapper.class)
@ContextConfiguration(classes = HostTestContextConfiguration.class)
@ActiveProfiles("test")
@Slf4j
class CucumberSpringConfiguration {
}
