package sinnet.web;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

// For dev and local environment we need to add two extrac accounts to test some scenarios
// we need extrac env variable available after deployment
// INGRESS_HOST
// sinnet.local -> is is deployed locally (in minikube)
// localhost -> is is deployed locally (manually)
// any other - non-dev env
@Configuration
class JwtLocalDecoderConfigurer {

  /**
   * For test purpose we would like to produce jwt token expected by application
   * logic, and dictated by http request
   * created only to satisfy test scenarios.
   * Inspired by https://www.baeldung.com/spring-security-oauth-jwt
   */
  @Bean
  @Conditional(OnLocalEnvironment.class)
  public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {

    return token -> {
      var secret = "my super secret key to sign my dev JWT token";
      var originalKey = new SecretKeySpec(secret.getBytes(), "HS256");
      var decoder = NimbusJwtDecoder
          .withSecretKey(originalKey)
          .build();
      var jwt = decoder.decode(token);
      return jwt;
    };
  }


  // we have 3 cases where we would like to accept any tokens:
  static class OnLocalEnvironment extends AnyNestedCondition {

    public OnLocalEnvironment() {
      super(ConfigurationPhase.REGISTER_BEAN);
    }
  
    @ConditionalOnProperty(value = "INGRESS_HOST", havingValue = "test")
    static class OnTestEnv {}
  
    @ConditionalOnProperty(value = "INGRESS_HOST", havingValue = "localhost")
    static class OnLocalEnv {}
  
    @ConditionalOnProperty(value = "INGRESS_HOST", havingValue = "sinnet.local")
    static class OnSinnetLocalEnv {}
  }
  
}

