package sinnet.grpc.actions;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.grpc.mapping.RpcCommandHandler;
import sinnet.grpc.timeentries.RemoveCommand;
import sinnet.grpc.timeentries.RemoveResult;
import sinnet.write.ActionRepositoryEx;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class TimeEntriesRpcRemove implements
    RpcCommandHandler<RemoveCommand, RemoveResult>,
    sinnet.grpc.common.Mapper {

  private final ActionRepositoryEx actionService;

  @Override
  public RemoveResult apply(RemoveCommand request) {
    var id = fromDto(request.getEntityId());
    var result = actionService.remove(id);
    return RemoveResult.newBuilder()
        .setResult(result)
        .build();
  }

}
