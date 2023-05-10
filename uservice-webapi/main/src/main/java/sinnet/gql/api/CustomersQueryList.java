package sinnet.gql.api;


import org.springframework.stereotype.Controller;

import io.vavr.collection.Iterator;
import lombok.RequiredArgsConstructor;
import sinnet.gql.models.CustomerEntity;
import sinnet.grpc.CustomersGrpcService;
import sinnet.grpc.customers.ListRequest;

@Controller
@RequiredArgsConstructor
class CustomersQueryList implements CustomerMapper {

  private final CustomersGrpcService service;

  public CustomerEntity[] list(CustomersQuery self) {
    var request = ListRequest.newBuilder()
        .setProjectId(self.getProjectId())
        .setUserToken(self.getUserToken())
        .build();
    var items = service.list(request);
    return Iterator.ofAll(items.getCustomersList()).map(this::toGql).toJavaArray(CustomerEntity[]::new);
  }

}
