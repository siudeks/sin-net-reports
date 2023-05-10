package sinnet.gql.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;

import io.quarkus.arc.Priority;
import io.quarkus.arc.profile.IfBuildProfile;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipalFactory;
import io.smallrye.jwt.auth.principal.ParseException;

/**
 * JWI decoder to extract Principal for dummy JWT. Used only on dev so that in tests we may use any handcrafted JWT token in requests
 * more: https://smallrye.io/docs/smallrye-jwt/configuration.html#_custom_factories
 */
@ApplicationScoped
@Alternative
@Priority(1)
@IfBuildProfile("dev")
public class DevTokenDecoder extends JWTCallerPrincipalFactory {

  @Override
  public JWTCallerPrincipal parse(String token, JWTAuthContextInfo authContextInfo) throws ParseException {
      try {
          // Token has already been verified, parse the token claims only
          String json = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
          return new DefaultJWTCallerPrincipal(JwtClaims.parse(json));
      } catch (InvalidJwtException ex) {
          throw new ParseException(ex.getMessage());
      }
  }
}
