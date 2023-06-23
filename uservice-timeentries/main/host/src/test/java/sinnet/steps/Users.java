package sinnet.steps;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import sinnet.models.ValEmail;
import sinnet.models.ValName;

@RequiredArgsConstructor
public class Users {

  private final TestApi testApi;

  @Autowired
  public ExpectedState ctx;

  private static ValEmail newEmail() {
        return ValEmail.of("test-user@" + RandomStringUtils.randomAlphabetic(8));
  }

  @When("logged user called {operatorAlias}")
  public void logged_user(ValName userAlias) {
    ctx.onKnownSession(userAlias, newEmail());
    ctx.onActiveUser(userAlias);
  }

  @Given("known user called {operatorAlias}")
  public void know_user_called_userAlias(ValName operatorAlias) {
    ctx.onKnownSession(operatorAlias, newEmail());
  }

}
