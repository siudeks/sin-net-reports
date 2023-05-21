package onlexnet.sinnet.actests;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = { ContextConfigurer.class })
@ComponentScan("onlexnet.sinnet.actests.steps")
public class CucumberContextConfigurer {
}
