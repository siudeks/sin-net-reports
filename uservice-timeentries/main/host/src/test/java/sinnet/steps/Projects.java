package sinnet.steps;

import java.util.UUID;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.val;
import sinnet.models.ValName;

@RequiredArgsConstructor
public class Projects {

  private final TestApi testApi;

  private ClientContext user;

  @Before
  public void initScenarion() {
    user = new ClientContext();
    user.currentOperator = ValName.of("Operator [" + UUID.randomUUID() + "]");
  }

  @When("user creates new project")
  public void a_new_project_called() {
    val projectAlias = ValName.of("NewProject [" + UUID.randomUUID() + "]");
    testApi.createNewProject(user, projectAlias);
  }

  @Then("they can rename the project")
  public void they_can_rename_the_project() {
    var currentProject = user.currentProject();
    var newName = currentProject.getValue() + "(2)";
    testApi.updateProject(user, currentProject, newName);
  }

}
