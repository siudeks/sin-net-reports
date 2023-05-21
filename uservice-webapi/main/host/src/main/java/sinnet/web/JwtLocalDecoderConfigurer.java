package sinnet.web;

import java.text.ParseException;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.JWTProcessor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sinnet.Profiles;

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
  @Profile(Profiles.Jwt.Local)
  public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties maybeUsedInTheFutureCurrentlyIgnored) {

    var jwtProcessor =  new UnsafeJwtProcessor();
    var decoder = new NimbusJwtDecoder(jwtProcessor);

    return token -> {
      var jwt = decoder.decode(token);
      return jwt;
    };
  }

  /**
   * Compatible with Nimbus processu used to just parse JWT tokens, ignoring any validation.
   * Designed only for tests purposes.
   */
  static class UnsafeJwtProcessor implements JWTProcessor<SecurityContext> {

    @Override
    @SneakyThrows
    public JWTClaimsSet process(String jwtString, SecurityContext context) {
      return JWTParser.parse(jwtString).getJWTClaimsSet();
    }

    @Override
    @SneakyThrows
    public JWTClaimsSet process(JWT jwt, SecurityContext context) {
      return jwt.getJWTClaimsSet();
    }

    @Override
    @SneakyThrows
    public JWTClaimsSet process(PlainJWT jwt, SecurityContext context) {
      return jwt.getJWTClaimsSet();
    }

    @Override
    @SneakyThrows
    public JWTClaimsSet process(SignedJWT jwt, SecurityContext context) {
      return jwt.getJWTClaimsSet();
    }

    @Override
    @SneakyThrows
    public JWTClaimsSet process(EncryptedJWT jwt, SecurityContext context) {
      return jwt.getJWTClaimsSet();
    }
    
  }
}
