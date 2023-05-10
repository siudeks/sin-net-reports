package sinnet.grpc.customers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sinnet.grpc.mapping.RpcQueryHandler;

@Component
@RequiredArgsConstructor
class CustomersRpcList implements RpcQueryHandler<ListRequest, ListReply>,
                       MapperDto,
                       MapperDbo {

  private final CustomerRepository repository;

  @Override
  public ListReply apply(ListRequest request) {
    var projectId = UUID.fromString(request.getProjectId());
    var result = repository.findByProjectId(projectId).map(this::fromDbo);
    var response = result.map(this::toDto).toList();

    return ListReply.newBuilder()
        .addAllCustomers(response)
        .build();
  }

}
