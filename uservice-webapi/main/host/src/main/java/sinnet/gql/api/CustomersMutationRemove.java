package sinnet.gql.api;


import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import sinnet.gql.models.EntityGql;
import sinnet.grpc.CustomersGrpcService;
import sinnet.grpc.customers.RemoveRequest;

@Controller
@RequiredArgsConstructor
class CustomersMutationRemove implements CustomerMapper {

  private final CustomersGrpcService service;

  public Boolean remove(CustomersMutation self, @Argument EntityGql id) {
    var cmd = RemoveRequest.newBuilder()
        .setEntityId(toGrpc(id))
        .setUserToken(self.getUserToken())
        .build();
    var result = service.remove(cmd);
    return result.getSuccess();
  }
}
