package onlexnet.sinnet.actests.steps;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import onlexnet.sinnet.actests.support.Sessions;
import onlexnet.sinnet.actests.support.UserEmail;

public class ProjectManagement {

  @Autowired
  AzureAD azureAd;

  @Autowired
  UserLoginProps userLoginProps;

  private Sessions session = new Sessions();

  @Given("a person named {userName}")
  public void a_person_named_user2(UserEmail email) {
    session.given(email);
  }

  @Then("Number of projects is {int}")
  public void number_of_projects_is(int numberOfProjects) {
    // var ctx = session.getActiveUser();
    // var actual = ctx.appApi.projectsCount().getNumberOfProjects();
    // assertThat(actual).isEqualTo(numberOfProjects);
    throw new PendingException();
  }

  @Then("the project is visible on the list of projects")
  public void the_project_is_visible_on_the_list_of_projects() {
    // var ctx = session.getActiveUser();
    // var project = ctx.state.getLastCreatedProject();
    // var expectedName = project.entity().getName();
    // var projects = ctx.appApi.projectList(expectedName);
    // assertThat(projects.getList().stream().map(it -> it.getName())).contains(expectedName);
    throw new PendingException();
  }

  @When("User {userName} creates new project")
  public void user_creates_new_project(UserEmail email) {
    var projectAlias = "My new project";
    var ctx = session.tryGet(email);
    ctx.appApi.createProject(projectAlias);
  }

  @When("User {userName} deletes lastly created project")
  public void user_deletes_lastly_create_project(UserEmail email) {
    // var ctx = session.tryGet(email);
    // var id = ctx.state.getLastCreatedProject();
    // var projectId = new AppApi.ProjectId(id.entity().getEntity().getEntityId(), 0);
    // ctx.appApi.removeProject(projectId);
    throw new PendingException();
  }

  @When("{userName} creates maximum of free projects")
  public void user1_creates_maximum_of_free_projects(UserEmail userEmail) {
    // val maximumOfProjects = 10; // business decision, undocumented
    // var ctx = session.tryGet(userEmail);
    // Stream.rangeClosed(1, maximumOfProjects).forEach(projectNo -> {
    // ctx.appApi.createProject("new Project " + projectNo);
    // });
    // throw new PendingException();
  }

  @Then("The user can't create more projects")
  public void the_user_can_t_create_more_projects() {
    // var ctx = session.getActiveUser();
    // var randomProjectName = RandomStringUtils.randomAlphanumeric(6);
    // Assertions
    // .assertThatCode(() -> ctx.appApi.createProject("New Extra Project " +
    // randomProjectName))
    // .isInstanceOf(GraphQLClientException.class);
    throw new PendingException();
  }
}
