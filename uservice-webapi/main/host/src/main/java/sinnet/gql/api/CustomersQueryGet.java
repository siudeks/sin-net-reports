package sinnet.gql.api;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import sinnet.gql.models.CustomerEntity;
import sinnet.grpc.CustomersGrpcService;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.customers.GetRequest;

@Controller
@RequiredArgsConstructor
class CustomersQueryGet implements CustomerMapper {

  private final CustomersGrpcService service;

  public CustomerEntity get(CustomersQuery self, @Argument String entityId) {
    var request = GetRequest.newBuilder()
        .setEntityId(EntityId.newBuilder()
            .setProjectId(self.getProjectId())
            .setEntityId(entityId))
        .build();
    var result = service.get(request);
    return this.toGql(result);
  }

}
