package sinnet.steps;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import sinnet.models.ValName;

@RequiredArgsConstructor
public class Projects {

  private final TestApi testApi;

  @Autowired
  public ExpectedState ctx;

  @When("user creates new project named {projectAlias}")
  public void a_new_project_called(ValName projectAlias) {
    testApi.createNewProject(ctx, projectAlias);
  }

  @Then("they can rename the project")
  public void they_can_rename_the_project() {
    var expected = ctx.buildExpectedState();
    var currentProject = expected.activeProject();
    var newName = currentProject.getValue() + "(2)";
    testApi.updateProject(ctx, currentProject, newName);
  }

  @Then("number of available projects is {int}")
  public void number_of_available_projects_is_arg1(int expected) {
    var numerOfProjects = testApi.numberOfProjects(ctx);
    Assertions.assertThat(numerOfProjects).isEqualTo(expected);
  }

  @Then("the project name is as expected")
  public void the_project_name_is_as_expected() {
    var expected = ctx.buildExpectedState();
    var activeProject = expected.activeProject();
    var projectModel = expected.getProjectId(activeProject).get();
    
    var actual = testApi.getProject(projectModel.id());
    Assertions.assertThat(actual.name()).isEqualTo(projectModel.name());
  }

}
