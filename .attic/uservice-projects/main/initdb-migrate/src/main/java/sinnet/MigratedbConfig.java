package sinnet;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
  @PropertySource("classpath:migratedb.properties"),
  @PropertySource(
      value = "classpath:migratedb-${spring.profiles.active:default}.properties",
      ignoreResourceNotFound = true)
})
class MigratedbConfig {
  
}


