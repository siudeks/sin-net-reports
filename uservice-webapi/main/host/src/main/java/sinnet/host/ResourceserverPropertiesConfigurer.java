package sinnet.host;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

interface ResourceserverPropertiesConfigurer {

  @PropertySource(value = "classpath:resourceserver-issuerB2C")
  @Profile("issuerB2C")
  public class ResourceserverIssuerB2C {
  }

  @PropertySource(value = "classpath:resourceserver-issuerLocal")
  @Profile("issuerLocal")
  public class ResourceserverIssuerLocal {
  }
}
