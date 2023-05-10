package sinnet.gql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.scalars.ExtendedScalars;

@Configuration
class ScalarConfiguration {
  
  @Bean
  RuntimeWiringConfigurer configurer() {
    return builder -> builder
      .scalar(ExtendedScalars.Date)
      .scalar(ExtendedScalars.GraphQLLong);
  }
}
