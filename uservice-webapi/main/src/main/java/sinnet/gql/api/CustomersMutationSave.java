package sinnet.gql.api;

import org.springframework.stereotype.Controller;

import io.vavr.collection.Array;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.models.CustomerContactInput;
import sinnet.gql.models.CustomerInput;
import sinnet.gql.models.CustomerSecretExInput;
import sinnet.gql.models.CustomerSecretInput;
import sinnet.gql.models.EntityGql;
import sinnet.gql.models.SomeEntityGql;
import sinnet.grpc.CustomersGrpcService;
import sinnet.grpc.customers.CustomerModel;
import sinnet.grpc.customers.UpdateCommand;

/** Fixme. */
@Controller
@RequiredArgsConstructor
class CustomersMutationSave implements CustomerMapper {

  private final CustomersGrpcService service;

  public SomeEntityGql save(CustomersMutation self, EntityGql id, CustomerInput entry,
      CustomerSecretInput[] secrets,
      CustomerSecretExInput[] secretsEx,
      CustomerContactInput[] contacts) {

    var request = UpdateCommand.newBuilder()
        .setUserToken(self.getUserToken())
        .setModel(CustomerModel.newBuilder()
            .setId(toGrpc(id))
            .setValue(toGrpc(entry))
            .addAllSecrets(Array.of(secrets).map(this::toGrpc))
            .addAllSecretEx(Array.of(secretsEx).map(this::toGrpc))
            .addAllContacts(Array.of(contacts).map(this::toGrpc))
            .build())
        .build();
    var result = service.update(request);
    return this.toGql(result.getEntityId());
  }

}
