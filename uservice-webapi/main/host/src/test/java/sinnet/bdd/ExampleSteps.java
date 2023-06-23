package sinnet.bdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import onlexnet.sinnet.webapi.test.AppQuery;
import sinnet.domain.ProjectId;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.gql.models.SomeEntityGql;
import sinnet.grpc.ProjectsGrpcFacade;
import sinnet.grpc.ProjectsGrpcFacade.StatsResult;

public class ExampleSteps {

  @Autowired
  ProjectsGrpcFacade projectsGrpc;

  @Autowired
  TestRestTemplate restTemplate;

  String requestorEmail;
  AppQuery appQuery;

  @Before
  public void before() {
    requestorEmail = "email@" + UUID.randomUUID();
    appQuery = new AppQuery(restTemplate.getRootUri(), requestorEmail);
    Mockito.clearInvocations(projectsGrpc);
  }

  @When("user is requesting list of projects")
  public void user_is_requesting_list_of_projects() {

    var grpcResult = new ProjectEntityGql()
        .setEntity(new SomeEntityGql()
            .setEntityId("1")
            .setEntityVersion(2L)
            .setProjectId("1"))
        .setName("my name");
    Mockito
        .when(projectsGrpc.list(eq(requestorEmail), any()))
        .thenReturn(List.of(grpcResult));

    appQuery.findProjectByName("spring-framework")
        .hasSizeGreaterThan(0);

  }

  @Then("Response is returned")
  public void response_is_returned() {
  }

  @Then("Project uservice is requested")
  public void Project_uservice_is_requested() {
    // Write code here that turns the phrase above into concrete actions
  }

  ProjectEntityGql expectedCreatedProject;
  ProjectEntityGql lastlyCreatedProject;
  Integer expectedNumberOfProjects;

  @When("a project is saved")
  public void project_is_saved() {
    var projectNewName = "projectname-" + UUID.randomUUID();

    var projectId1 = new ProjectId("1", 1L);
    Mockito
        .when(projectsGrpc.create(requestorEmail))
        .thenReturn(projectId1);
    var projectId2 = new ProjectId("1", 2L);
    Mockito
        .when(
            projectsGrpc.update(eq(requestorEmail), eq(projectId1), eq(projectNewName), eq(requestorEmail), eq(List.of())))
        .thenReturn(projectId2);
    var saveResult = appQuery.createProject(projectNewName);

    lastlyCreatedProject = saveResult.get();
    expectedCreatedProject = new ProjectEntityGql()
        .setEntity(new SomeEntityGql()
            .setEntityId("1")
            .setEntityVersion(2L)
            .setProjectId("1"))
        .setName(projectNewName);
  }

  @Then("operation result is returned")
  public void operation_result_is_returned() {
    assertThat(lastlyCreatedProject)
        .isEqualTo(expectedCreatedProject);
  }

  @When("userstats request is send to backend")
  public void userstats_request_is_send_to_backend() {

    expectedNumberOfProjects = new Random().nextInt();
    Mockito
        .when(projectsGrpc.userStats(requestorEmail))
        .thenReturn(new StatsResult(expectedNumberOfProjects));
  }

  @Then("userstats are returned")
  public void userstats_are_returned() {
    var numberOfProjects = appQuery.numberOfProjects().get();

    Mockito
        .verify(projectsGrpc)
        .userStats(requestorEmail);

    assertThat(numberOfProjects)
        .isEqualTo(expectedNumberOfProjects);
  }

}
