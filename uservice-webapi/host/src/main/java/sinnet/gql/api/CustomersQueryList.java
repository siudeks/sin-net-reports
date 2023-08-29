package sinnet.gql.api;


import org.springframework.stereotype.Controller;

import io.vavr.collection.Iterator;
import lombok.RequiredArgsConstructor;
import sinnet.gql.models.CustomerEntityGql;
import sinnet.grpc.CustomersGrpcFacade;
import sinnet.grpc.customers.ListRequest;

@Controller
@RequiredArgsConstructor
class CustomersQueryList implements CustomerMapper {

  private final CustomersGrpcFacade service;

  public CustomerEntityGql[] list(CustomersQuery self) {
    var request = ListRequest.newBuilder()
        .setProjectId(self.getProjectId())
        .setUserToken(self.getUserToken())
        .build();
    var items = service.list(request);
    return Iterator.ofAll(items.getCustomersList()).map(this::toGql).toJavaArray(CustomerEntityGql[]::new);
  }

}
