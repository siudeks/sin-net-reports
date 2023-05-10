package sinnet.grpc.customers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.grpc.mapping.RpcQueryHandler;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class CustomersRpcGet implements
    RpcQueryHandler<GetRequest, GetReply>,
    MapperDto,
    MapperDbo {

  private final CustomerRepository repository;

  @Override
  public GetReply apply(GetRequest request) {
    var projectId = UUID.fromString(request.getEntityId().getProjectId());
    var entityId = UUID.fromString(request.getEntityId().getEntityId());

    var dbo = repository.findByProjectIdAndEntityId(projectId, entityId);
    var result = this.fromDbo(dbo);
    var dto = this.toDto(result);
    return GetReply.newBuilder().setModel(dto).build();
  }

}
