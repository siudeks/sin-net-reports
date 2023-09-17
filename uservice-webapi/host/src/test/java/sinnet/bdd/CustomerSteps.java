package sinnet.bdd;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import onlexnet.sinnet.webapi.test.AppApi;
import sinnet.grpc.ActionsGrpcFacade;
import sinnet.grpc.CustomersGrpcFacade;
import sinnet.grpc.customers.CustomerModel;
import sinnet.grpc.customers.CustomerValue;
import sinnet.grpc.customers.GetReply;
import sinnet.grpc.customers.ListReply;

public class CustomerSteps {

  @Autowired
  CustomersGrpcFacade customersGrpc;

  @Autowired
  TestRestTemplate restTemplate;

  String requestorEmail;
  AppApi appApi;

  @Before
  public void before() {
    requestorEmail = "email@" + UUID.randomUUID();
    appApi = new AppApi(restTemplate.getRootUri(), requestorEmail);
    Mockito.clearInvocations(customersGrpc);
  }
  
  @When("Customer list request is send to backend")
  public void customer_list_request_is_send_to_backend() {
    var projectId = UUID.randomUUID();

    Mockito
      .when(customersGrpc.list(any()))
      .thenReturn(ListReply
        .newBuilder()
        .addAllCustomers(List.of()).build());

    appApi.listCustomers(projectId);
  }

  @Then("Customer list result is verified")
  public void customer_list_result_is_verified() {
      // Write code here that turns the phrase above into concrete actions
      // throw new io.cucumber.java.PendingException();
  }

  @When("Customer read request is send to backend")
  public void customer_read_request_is_send_to_backend() {
    Mockito
      .when(customersGrpc.get(any()) )
      .thenReturn(GetReply
        .newBuilder()
        .setModel(CustomerModel.newBuilder()
          .setValue(CustomerValue.newBuilder()
            .setBillingModel("my billing model")))
            .build());
    
    var projectId = UUID.randomUUID();
    var entityId = UUID.randomUUID();
    var response = appApi.getCustomer(projectId, entityId).get();

    Assertions.assertThat(response.getData().getBillingModel()).isEqualTo("my billing model");
  }

  @Then("Customer read result is verified")
  public void customer_read_result_is_verified() {
      // Write code here that turns the phrase above into concrete actions
      // throw new io.cucumber.java.PendingException();
  }

}
