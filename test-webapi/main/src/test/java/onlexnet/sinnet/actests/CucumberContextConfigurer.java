package onlexnet.sinnet.actests;

import org.springframework.test.context.ContextConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;
import onlexnet.sinnet.webapi.test.AppQuery;

@CucumberContextConfiguration
@ContextConfiguration(classes = { AppQuery.class })
public class CucumberContextConfigurer {
}
