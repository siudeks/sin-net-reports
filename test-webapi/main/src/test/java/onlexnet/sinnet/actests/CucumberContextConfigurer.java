package onlexnet.sinnet.actests;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;
import onlexnet.sinnet.webapi.test.AppQuery;

@CucumberContextConfiguration
@ContextConfiguration(classes = { AppQuery.class, ContextConfigurer.class })
@ComponentScan("onlexnet.sinnet.actests.steps")
public class CucumberContextConfigurer {
}
