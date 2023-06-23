package sinnet.steps;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import sinnet.models.ValName;

@RequiredArgsConstructor
public class Timeentries {

  private final TestApi testApi;

  @Autowired
  public ExpectedState ctx;

  @Given("a new project called {projectAlias} is created by {operatorAlias}")
  public void a_new_project_called(ValName projectAlias, ValName operatorAlias) {
    testApi.createNewProject(ctx, projectAlias);
  }

  @Given("an operator called {operatorAlias} assigned to project called {projectAlias}")
  public void an_operator_called_alias1_assigned_toproject_called_alias2(ValName operatorAlias, ValName projectAlias) {
    testApi.addOperator(ctx, operatorAlias, projectAlias);
  }

  @When("the {operatorAlias} creates new timeentry")
  public void operator_called_creates_new_timeentry(ValName operatorName) {
    ctx.onActiveUser(operatorName);
    testApi.createEntry(ctx);
  }

  @Then("the new timeentry is visible on the {projectAlias}")
  public void the_new_timeentry_is_visible_on_the_projectAlias(ValName projectAlias) {
    var facts = ctx.buildExpectedState();
    var latestTimeentryId = facts.latestTimeentryId();
    var timeentryState = facts.latestTimeentry();
    var items = testApi.listTimeentries(ctx, projectAlias, timeentryState.when());
    Assertions.assertThat(items).contains(latestTimeentryId);
  }
}
