package bdd;

import static org.mockito.ArgumentMatchers.any;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.grpc.ProjectsGrpcService;
import sinnet.grpc.projects.generated.ListReply;

public class StepDefinitions {

  @Autowired
  ProjectsGrpcService projectsGrpcService;

  @Autowired
  TestRestTemplate restTemplate;

  @Before
  public void before() {
    Mockito.clearInvocations(projectsGrpcService);
  }

  @When("user is requesting list of projects")
  public void user_is_requesting_list_of_projects() {

    var grpcResult = ListReply.newBuilder()
        .addProjects(sinnet.grpc.projects.generated.Project.newBuilder()
            .build())
        .build();
    Mockito
        .when(projectsGrpcService.list(any()))
        .thenReturn(grpcResult);

    // Mockito.when(nameProvider.getName()).thenReturn("my name");
    var rootUri = restTemplate.getRootUri();

    var client = WebTestClient.bindToServer()
        .responseTimeout(Duration.ofMinutes(10))
        .baseUrl(rootUri + "/graphql")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + createTestJwt())
        .build();

    var tester = HttpGraphQlTester.create(client);

    var actual = tester.documentName("namedProjects")
        .variable("projectName", "spring-framework")
        .execute()
        .path("Projects.list")
        .entityList(ProjectEntityGql.class)
        .hasSizeGreaterThan(0);

  }

  @Then("Response is returned")
  public void response_is_returned() {
  }

  @Then("Project uservice is requested")
  public void Project_uservice_is_requested() {
    // Write code here that turns the phrase above into concrete actions
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
