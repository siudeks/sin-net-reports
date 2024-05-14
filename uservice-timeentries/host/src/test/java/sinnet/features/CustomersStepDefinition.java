package sinnet.features;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import sinnet.models.CustomerSecretEx;
import sinnet.models.CustomerValue;
import sinnet.models.OtpType;
import sinnet.models.ValName;

@RequiredArgsConstructor
public class CustomersStepDefinition {

  private final TestApi testApi;
  private final ClientContext ctx;

  private ValName singleCustomer = ValName.of("example customer");

  @When("{operatorAlias} creates a new customer")
  public void the_operator_creates_a_new_customer(ValName operatorAlias) {
    testApi.reserveCustomer(ctx, operatorAlias);
  }

  @Then("{operatorAlias} is able to change the name of the Customer")
  public void the_operator_is_able_to_change_the_name_of_the_customer(ValName operatorAlias) {
    testApi.updateReservedCustomer(ctx, singleCustomer, new CustomerValue().customerName(ValName.of("new-name-1")), List.of());

    testApi.customerExists(ctx, operatorAlias, "new-name-1");
    testApi.updateReservedCustomer(ctx, singleCustomer, new CustomerValue().customerName(ValName.of("new-name-2")), List.of());

    testApi.customerExists(ctx, operatorAlias, "new-name-2");
  }

  @Then("{operatorAlias} is able to change all properties of the Customer")
  public void operator1_is_able_to_change_all_properties_of_the_c_ustomer(ValName operatorAlias) {
    var customerValue = new CustomerValue()
        .customerName(ValName.of("new-name-1"))
        .billingModel("my-billing-model");
    var secretExt = new CustomerSecretEx()
        .setChangedWhen(LocalDateTime.of(2001,2,3,4,5,6))
        .setCodeType(new OtpType.Totp("my secret", 42));
    testApi.updateReservedCustomer(ctx, singleCustomer, customerValue, List.of(secretExt));
    var customer = testApi.getCustomer(ctx, singleCustomer, operatorAlias);

    Assertions.assertThat(customer.getValue().customerName().getValue()).isEqualTo("new-name-1");
    Assertions.assertThat(customer.getValue().billingModel()).isEqualTo("my-billing-model");
    Assertions.assertThat(customer.getSecretsEx()).isNotEmpty();
    var actual = customer.getSecretsEx().get(0);
    Assertions.assertThat(actual.getCodeType()).isEqualTo(new OtpType.Totp("my secret", 42));

  }

}
