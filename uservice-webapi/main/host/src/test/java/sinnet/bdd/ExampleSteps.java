package sinnet.bdd;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import onlexnet.sinnet.webapi.test.AppQuery;
import sinnet.domain.ProjectId;
import sinnet.gql.mappers.ProjectsMapper;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.gql.models.SomeEntityGql;
import sinnet.grpc.ProjectsGrpcFacade;

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

  @When("a project is saved")
  public void project_is_saved() {
    var projectName = "projectname-" + UUID.randomUUID();

    var projectId1 = new ProjectId("1", 1L);
    Mockito
        .when(projectsGrpc.create(requestorEmail))
        .thenReturn(projectId1);
    var projectId2 = new ProjectId("1", 2L);
    Mockito
        .when(projectsGrpc.update(eq(requestorEmail), eq(projectId1), eq(projectName), eq(requestorEmail), eq(List.of())))
        .thenReturn(projectId2);
    var saveResult = appQuery.createProject(projectName);

    lastlyCreatedProject = saveResult.get();
    expectedCreatedProject = new ProjectEntityGql()
      .setEntity(new SomeEntityGql()
        .setEntityId("1")
        .setEntityVersion(2L)
        .setProjectId("1"))
      .setName(projectName);
  }

  @Then("operation result is returned")
  public void operation_result_is_returned() {
    Assertions
      .assertThat(lastlyCreatedProject)
      .isEqualTo(expectedCreatedProject);
  }
}
