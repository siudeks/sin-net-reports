package onlexnet.sinnet.webapi.test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/** TBD. */
public class Jwt {
  
  /** JWT. */
  public static String createTestJwt(String email) {
    var secret = "my super secret key to sign my dev JWT token";
    return JWT.create()
        .withSubject(email)
        .withIssuer("https://issuer")
        .withClaim("emails", List.of(email))
        .withIssuedAt(Instant.now())
        .withExpiresAt(ZonedDateTime.now().plusHours(1).toInstant())
        .sign(Algorithm.HMAC256(secret));
  }

}
