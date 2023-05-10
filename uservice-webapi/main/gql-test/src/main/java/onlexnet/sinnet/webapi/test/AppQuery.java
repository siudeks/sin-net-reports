package onlexnet.sinnet.webapi.test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.EntityList;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sinnet.gql.models.ProjectEntityGql;

/** Set of methods used to test application functionlity. */
@Component
public class AppQuery {

  /** TBD. */
  public EntityList<ProjectEntityGql> fundProjectByName(String rootUri, String projectName) {

    var client = WebTestClient.bindToServer()
        .responseTimeout(Duration.ofMinutes(10))
        .baseUrl(rootUri + "/graphql")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + createTestJwt())
        .build();

    var tester = HttpGraphQlTester.create(client);

    return tester.documentName("namedProjects")
        .variable("projectName", projectName)
        .execute()
        .path("Projects.list")
        .entityList(ProjectEntityGql.class);
  }

  String createTestJwt() {
    var secret = "my super secret key to sign my dev JWT token";
    return JWT.create()
        .withSubject("a@b.c")
        .withIssuer("https://issuer")
        .withClaim("emails", List.of("a@b.c"))
        .withIssuedAt(Instant.now())
        .withExpiresAt(ZonedDateTime.now().plusHours(1).toInstant())
        .sign(Algorithm.HMAC256(secret));
  }

}
