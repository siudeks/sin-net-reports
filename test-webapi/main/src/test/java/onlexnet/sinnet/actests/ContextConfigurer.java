package onlexnet.sinnet.actests;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackageClasses = ContextConfigurer.class)
@PropertySource(value = "application.properties")
public class ContextConfigurer {
}
