package sinnet.bdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import onlexnet.sinnet.webapi.test.AppApi;
import sinnet.domain.ProjectId;
import sinnet.gql.api.CommonMapper;
import sinnet.gql.models.CustomerInput;
import sinnet.gql.models.CustomerSecretExInput;
import sinnet.gql.models.CustomerSecretInput;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.gql.models.SomeEntityGql;
import sinnet.gql.models.UserGql;
import sinnet.grpc.CustomersGrpcFacade;
import sinnet.grpc.ProjectsGrpcFacade;
import sinnet.grpc.ProjectsGrpcFacade.StatsResult;
import sinnet.grpc.UsersGrpcService;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.customers.CustomerModel;
import sinnet.grpc.customers.CustomerSecret;
import sinnet.grpc.customers.CustomerSecretEx;
import sinnet.grpc.customers.CustomerValue;
import sinnet.grpc.customers.UpdateCommand;
import sinnet.grpc.customers.UpdateResult;
import sinnet.grpc.users.UsersSearchModel;
import sinnet.infra.TimeProvider;

public class StepDefinitions {

  @Autowired
  ProjectsGrpcFacade projectsGrpc;

  @Autowired
  CustomersGrpcFacade customersGrpc;

  @Autowired
  UsersGrpcService usersGrpc;

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  TimeProvider timeProvider;

  String requestorEmail;
  AppApi appApi;

  @Before
  public void before() {
    requestorEmail = "email@" + UUID.randomUUID();
    appApi = new AppApi(restTemplate.getRootUri(), requestorEmail);
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

    appApi.findProjectByName("spring-framework")
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
    var saveResult = appApi.createProject(projectNewName);

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
    var numberOfProjects = appApi.numberOfProjects().get();

    Mockito
        .verify(projectsGrpc)
        .userStats(requestorEmail);

    assertThat(numberOfProjects)
        .isEqualTo(expectedNumberOfProjects);
  }

  Runnable reserveValidation;

  @When("Customer creation request is send to backend")
  public void Customer_creation_request_is_send_to_backend() {
    var projectId = "projectId [" + UUID.randomUUID() + "]";

    var projectVersion = 1L;
    var expectedCmd = sinnet.grpc.customers.ReserveRequest.newBuilder()
        .setProjectId(projectId)
        .build();
    var expectedResult = sinnet.grpc.customers.ReserveReply.newBuilder()
        .setEntityId(EntityId.newBuilder()
            .setEntityId(projectId)
            .setEntityVersion(projectVersion)
            .build())
        .build();

    Mockito
      .when(customersGrpc.reserve(expectedCmd))
      .thenReturn(expectedResult);

    reserveValidation = () -> {
      var reserveCustomerResult = appApi.reserveCustomer(projectId).get();;
      assertThat(reserveCustomerResult.getEntityId()).isEqualTo(projectId);
      assertThat(reserveCustomerResult.getEntityVersion()).isEqualTo(projectVersion);
    };
  }

  @Then("Customer creation result is verified")
  public void Customer_creation_result_is_returned() {
      reserveValidation.run();
      reserveValidation = null;
  }

  @When("Users list query is send")
  public void Users_list_query_is_send() {
    var projectId = "projectId [" + UUID.randomUUID() + "]";

    var query = sinnet.grpc.users.SearchRequest.newBuilder()
        .setUserToken(UserToken.newBuilder()
            .setRequestorEmail(requestorEmail)
            .setProjectId(projectId))
        .build();
    var reply = sinnet.grpc.users.SearchReply.newBuilder()
        .addItems(UsersSearchModel.newBuilder()
          .setEmail("my email")
          .setEntityId("my entity id"))
        .build();

    Mockito
      .when(usersGrpc.search(query))
      .thenReturn(reply);

    userListValidation = () -> {
      var response = appApi.searchUsers(projectId).get();
      assertThat(response)
        .containsOnly(new UserGql("my email", "my entity id"));
    };

  }

  Runnable userListValidation;

  @Then("Users list response is returned")
  public void Users_list_response_is_returned() {
    userListValidation.run();
    userListValidation = null;
  }

  @When("Customer save request is send to backend")
  public void customer_save_request_is_send_to_backend() {
    var projectId = UUID.randomUUID();
    var projectIdStr = projectId.toString();
    var entityId = UUID.randomUUID();
    var entityIdStr = entityId.toString();

    var argumentCaptor = ArgumentCaptor.forClass(UpdateCommand.class);
    Mockito
      .when(customersGrpc.update(argumentCaptor.capture()))
      .thenReturn(UpdateResult.newBuilder()
        .setEntityId(EntityId.newBuilder().setProjectId(projectIdStr).setEntityId(entityIdStr).setEntityVersion(42L)
          .setProjectId(projectId.toString())
          .setEntityId(entityId.toString())
          .setEntityVersion(43))
          .build());

      var secret = new CustomerSecretInput()
          .setLocation("location 1")
          .setPassword("password 1")
          .setUsername("username 1")
          .setOtpSecret("my secret 1")
          .setOtpRecoveryKeys("my key 1");
      var secretExt = new CustomerSecretExInput()
        .setEntityCode("entity code 2")
        .setEntityName("entity name 2")
        .setLocation("location 2")
        .setPassword("password 2")
        .setUsername("username 2")
        .setOtpSecret("my secret 2")
        .setOtpRecoveryKeys("my key 2");
    var actual = appApi.saveCustomer(
      projectId,
      new SomeEntityGql().setProjectId(projectIdStr).setEntityId(entityIdStr).setEntityVersion(42L),
      new CustomerInput(),
      List.of(secret),
      List.of(secretExt),
      List.of()).get();

    var expectedResult = new SomeEntityGql().setProjectId(projectIdStr).setEntityId(entityId.toString()).setEntityVersion(43L);
    Assertions.assertThat(actual).isEqualTo(expectedResult);

    var expectedCommand = UpdateCommand.newBuilder()
        .setUserToken(UserToken.newBuilder()
          .setProjectId(projectId.toString())
          .setRequestorEmail(requestorEmail))
        .setModel(CustomerModel.newBuilder()
          .setId(EntityId.newBuilder()
            .setProjectId(projectIdStr)
            .setEntityId(entityIdStr)
            .setEntityVersion(42))
          .addSecrets(CustomerSecret.newBuilder()
            .setLocation("location 1")
            .setPassword("password 1")
            .setUsername("username 1")
            .setOtpSecret("my secret 1")
            .setOtpRecoveryKeys("my key 1")
            .setChangedWho(requestorEmail)
            .setChangedWhen(CommonMapper.toGrpc(timeProvider.now())))
          .addSecretEx(CustomerSecretEx.newBuilder()
            .setEntityCode("entity code 2")
            .setEntityName("entity name 2")
            .setLocation("location 2")
            .setPassword("password 2")
            .setUsername("username 2")
            .setOtpSecret("my secret 2")
            .setOtpRecoveryKeys("my key 2")
            .setChangedWho(requestorEmail)
            .setChangedWhen(CommonMapper.toGrpc(timeProvider.now())))
          .setValue(CustomerValue.newBuilder()))
        .build();
    Assertions.assertThat(argumentCaptor.getValue())
        .isEqualTo(expectedCommand);
  }

  @Then("Customer save result is verified")
  public void customer_save_result_is_verified() {
  }



}

