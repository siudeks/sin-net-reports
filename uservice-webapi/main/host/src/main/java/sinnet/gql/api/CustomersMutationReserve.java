package sinnet.gql.api;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import sinnet.gql.models.EntityGql;
import sinnet.grpc.CustomersGrpcService;
import sinnet.grpc.customers.ReserveRequest;

@Controller
@RequiredArgsConstructor
class CustomersMutationReserve implements CommonMapper {

  private final CustomersGrpcService service;

  @SchemaMapping
  public EntityGql reserve(CustomersMutation self) {
    var request = ReserveRequest.newBuilder()
        .setProjectId(self.getProjectId())
        .build();
    var result = service.reserve(request);
    return CommonMapper.toGql(result.getEntityId());
  }
}
