package net.onlex;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java8.PendingException;
import net.onlex.support.Project;
import net.onlex.support.Sessions;
import net.onlex.support.UserEmail;

public final class TimesheetsOperations {

  private Sessions sessions = new Sessions();

  @Given("a project called {projectName} created by {userName}")
  public void a_project_called_term1_created_by_term2(Project project, UserEmail owner) {
    var ctx = sessions.getOrCreate(owner);
    ctx.appApi.createProject(project.getAlias());
  }


  @When("{userName} creates timeentry for {projectName} owned by {userName}")
  public void user_creates_timeentry_for_project(UserEmail operator, Project project, UserEmail projectOwner) {
    var projectAlias = project.getAlias();
    var ownerCtx = sessions.getOrCreate(projectOwner);
    var maybeProject = ownerCtx.getState().getProjectByAlias(projectAlias);
    var ctx = sessions.getOrCreate(operator);
    var projectId = maybeProject.orElseThrow().entity().getEntity().getEntityId();
    ctx.appApi.createTimeentry(projectId);
    // !!! No assertion yet
    throw new io.cucumber.java.PendingException("{userName} creates timeentry for {projectName} owned by {userName}");
  }

  @Then("operation is rejected")
  public void operation_is_rejected() {
      // Write code here that turns the phrase above into concrete actions
      throw new io.cucumber.java.PendingException("operation is rejected");
  }

  @Then("number of timesheets in {projectName} is zero")
  public void number_of_timesheets_in_project_is_zero(Project project) {
      // Write code here that turns the phrase above into concrete actions
      throw new PendingException("number of timesheets in project {projectName} is zero");
  }

  @When("User {userName} assigns {userName} to {projectName}")
  public void owner_assigns_operator_to_project(UserEmail owner, UserEmail operator, Project project) {
    var ctx = sessions.getOrCreate(owner);
    var projectAlias = project.getAlias();
    var projectId = ctx.getState().getProjectByAlias(projectAlias).get();
    var operatorEmail = operator.getName();
    ctx.appApi.assignOperator(projectId, operatorEmail);
  }
}
