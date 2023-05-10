package sinnet;

import static io.restassured.RestAssured.given;
import static  org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.restassured.RestAssured;
import io.vavr.Function1;
import lombok.SneakyThrows;
import lombok.val;
import sinnet.db.PostgresDbExtension;
import sinnet.grpc.projects.generated.Project;
import sinnet.grpc.projects.generated.ProjectModel;
import sinnet.host.HostTestContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { HostTestContextConfiguration.class })
@ActiveProfiles(Profiles.TEST)
@ExtendWith(PostgresDbExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectsITest {

  @Autowired
  AppOperations operations;

  private static Function1<Project, String> mapProjectToName = Function1.of(Project::getModel)
      .andThen(ProjectModel::getName);

  
  @Autowired
  private TestRestTemplate testRestTemplate;
  
  @BeforeEach
  public void beforeEach() {
    var uriAsString = testRestTemplate.getRootUri();
    var currentPort = URI.create(uriAsString).getPort();
    RestAssured.port = currentPort;
  }

  @Test
  public void getHealth() {
    given()
        .when().get("/actuator/health")
        .then()
        .statusCode(200)
        .body("components.livenessState.status", Matchers.equalTo("UP"));
  }

  @Test
  public void getDatasourceHealth() {
    given()
        .when().get("/actuator/health")
        .then()
        .statusCode(200)
        .body("components.readinessState.status", Matchers.equalTo("UP"));
  }

  @Test
  void should_create() {
    var ownerName = operations.generateUserEmail();
    var projectId = operations.create(ownerName);

    var updatedId = operations.update(projectId, "my name", ownerName);
    assertThat(updatedId.getEId()).isEqualTo(projectId.getEId());
    assertThat(updatedId.getETag()).isEqualTo(projectId.getETag() + 1);

    var projectsOfOwner = operations.listOfProjects(ownerName, Function1.identity());
    assertThat(projectsOfOwner)
        .as("List of projects contains just created entry")
        .isNotEmpty();

    var item0 = projectsOfOwner.get(0);
    var item0Id = item0.getId();
    var expected0id = item0Id.toBuilder().setETag(projectId.getETag() + 1).build();
    var expected0model = item0.getModel();
    var expected = Project.newBuilder()
        .setId(expected0id)
        .setModel(expected0model)
        .build();
    assertThat(projectsOfOwner).contains(expected);
  }

  @Test
  void should_limit_number_of_free_projects() {
    var ownerName = operations.generateUserEmail();

    // lets create 10 free projects
    val limit_of_free_projects = 10;
    for (int i = 0; i < limit_of_free_projects; i++) {
      var created = operations.create(ownerName);
      operations.update(created, "my name", ownerName);
    }


    Assertions
        .assertThatCode(() -> operations.update(operations.create(ownerName), "my name", ownerName))
        .isInstanceOfSatisfying(StatusRuntimeException.class,
            ex -> assertThat(ex.getStatus().getCode()).isEqualTo(Status.RESOURCE_EXHAUSTED.getCode()));
  }

  @Test
  void should_remove() {
    var ownerName = operations.generateUserEmail();
    var projectId = operations.create(ownerName);

    operations.update(projectId, "my name", ownerName);
    operations.remove(projectId, ownerName);

    var projectsOfOwner = operations.listOfProjects(ownerName, Function1.identity());
    assertThat(projectsOfOwner)
        .as("List of projects does not contain just deleted project")
        .isEmpty();
  }

  @Test
  public void should_update() {
    var ownerEmail = operations.generateUserEmail();
    
    var projectId = operations.create(ownerEmail);

    var updateCmd = AppOperations.newUpdateCommand(projectId, "my name", ownerEmail, ownerEmail);
    var updateResult = operations.update(updateCmd);

    var newOwnerEmail = operations.generateUserEmail();
    updateCmd = AppOperations.newUpdateCommand(updateResult.getEntityId(), "my new name", newOwnerEmail, ownerEmail);
    operations.update(updateCmd);

    var projectsOfOwner = operations.listOfProjects(ownerEmail, mapProjectToName);
    assertThat(projectsOfOwner).isEmpty();

    var projectsOfNewOwner = operations.listOfProjects(newOwnerEmail, mapProjectToName);
    assertThat(projectsOfNewOwner).containsExactly("my new name");
  }

  @Test
  void should_reject_stale_updates_for_entity() {
    var ownerEmail = operations.generateUserEmail();
    var projectId = operations.create(ownerEmail);

    var updateCmdBuilder = AppOperations.newUpdateCommand(projectId, "my project name", ownerEmail, ownerEmail);
    operations.update(updateCmdBuilder);

    var newOwnerEmail = operations.generateUserEmail();
    var updateCmdBuilder2 = AppOperations.newUpdateCommand(projectId, "my new project name", newOwnerEmail, ownerEmail);

    Assertions.assertThatCode(() -> operations.update(updateCmdBuilder2))
        .isInstanceOfSatisfying(StatusRuntimeException.class,
            ex -> assertThat(ex.getStatus().getCode()).isEqualTo(Status.FAILED_PRECONDITION.getCode()));

    var actual = operations.listOfProjects(ownerEmail, mapProjectToName);
    assertThat(actual).containsExactly("my project name");
  }

  @Test
  @SneakyThrows
  void should_initialize_predefined_projects() {
    val knownOwnerOfPredefinedProjects = "siudeks@gmail.com";
    var actual = operations.listOfProjects(knownOwnerOfPredefinedProjects, Function1.identity());
    assertThat(actual).size().isNotZero();
  }

}
