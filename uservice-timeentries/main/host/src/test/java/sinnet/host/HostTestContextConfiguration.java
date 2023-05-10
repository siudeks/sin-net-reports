package sinnet.host;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HostConfiguration.class)
public class HostTestContextConfiguration {
  
}
