package sinnet.gql.customers;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLResolver;
import io.vertx.core.eventbus.EventBus;
import sinnet.gql.MyEntity;
import sinnet.gql.SomeEntity;
import sinnet.bus.AskTemplate;
import sinnet.bus.query.FindCustomer;

@Component
public class CustomersQueryGet extends AskTemplate<FindCustomer.Ask, FindCustomer.Reply>
                               implements GraphQLResolver<CustomersQuery> {

  public CustomersQueryGet(EventBus eventBus) {
    super(FindCustomer.Ask.ADDRESS, FindCustomer.Reply.class, eventBus);
  }

  CompletableFuture<Optional<CustomerEntity>> get(CustomersQuery gcontext, UUID entityId) {
    var query = new FindCustomer.Ask(gcontext.getProjectId(), entityId);
    return super
        .ask(query)
        .thenApply(it -> {
          var gqlId = new SomeEntity(gcontext.getProjectId(), it.getEntityId(), it.getEntityVersion());
          var gqlValue = it.getValue();
          var gqlSecrets = it.getSecrets();
          var gqlSecretsEx = it.getSecretsEx();
          var gqlContacts = it.getContacts();
          var result = new CustomerEntity(gqlId, gqlValue, gqlSecrets, gqlSecretsEx, gqlContacts);
          return Optional.of(result);
        });
  }
}

