package onlexnet.sinnet.webapi.test;

import java.time.Duration;

import org.springframework.graphql.test.tester.GraphQlTester.Entity;
import org.springframework.graphql.test.tester.GraphQlTester.EntityList;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import sinnet.gql.models.ProjectEntityGql;

/** Set of methods used to test application functionlity. */
public class AppQuery {

  private final HttpGraphQlTester tester;

  /** TBD. */
  public AppQuery(String rootUri, String email) {
    var token = Jwt.createTestJwt(email);

    var client = WebTestClient.bindToServer()
        .responseTimeout(Duration.ofMinutes(10))
        .baseUrl(rootUri + "/graphql")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .build();

    tester = HttpGraphQlTester.create(client);
  }

  /** TBD. */
  public EntityList<ProjectEntityGql> findProjectByName(String projectName) {

    return tester.documentName("namedProjects")
        .variable("projectName", projectName)
        .execute()
        .path("Projects.list")
        .entityList(ProjectEntityGql.class);
  }

  /** TBD. */
  public Entity<ProjectEntityGql, ?> createProject(String projectName) {
    return tester.documentName("projectCreate")
      .variable("name", projectName)
      .execute()
      .path("Projects.save")
      .entity(ProjectEntityGql.class);
  }

}
