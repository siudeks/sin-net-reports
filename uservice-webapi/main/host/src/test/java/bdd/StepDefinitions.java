package bdd;

import static org.mockito.ArgumentMatchers.any;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import onlexnet.sinnet.webapi.test.AppQuery;
import sinnet.grpc.ProjectsGrpcService;
import sinnet.grpc.projects.generated.ListReply;

public class StepDefinitions {

  @Autowired
  ProjectsGrpcService projectsGrpcService;

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  AppQuery appQuery;

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

    appQuery.fundProjectByName(rootUri, "spring-framework")
        .hasSizeGreaterThan(0);
    
  }

  @Then("Response is returned")
  public void response_is_returned() {
  }

  @Then("Project uservice is requested")
  public void Project_uservice_is_requested() {
    // Write code here that turns the phrase above into concrete actions
  }

}
