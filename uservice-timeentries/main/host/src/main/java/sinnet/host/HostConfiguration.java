package sinnet.host;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import sinnet.RootPackageMarker;

@SpringBootApplication
@ComponentScan(basePackageClasses = RootPackageMarker.class)
@EnableJpaRepositories(basePackageClasses = RootPackageMarker.class)
@EntityScan(basePackageClasses = RootPackageMarker.class)
class HostConfiguration {
}
