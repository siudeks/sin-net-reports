package sinnet.host;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import sinnet.RootPackageMarker;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = RootPackageMarker.class)
@EnableConfigurationProperties
class HostConfigurer {
}
