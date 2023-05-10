package sinnet.grpc.actions;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.grpc.mapping.RpcCommandHandler;
import sinnet.grpc.timeentries.ReserveCommand;
import sinnet.grpc.timeentries.ReserveResult;
import sinnet.grpc.timeentries.ReserveCommand.OptionalWhenCase;
import sinnet.models.ActionValue;
import sinnet.models.ShardedId;
import sinnet.models.ValEmail;
import sinnet.write.ActionRepositoryEx;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class TimeEntriesRpcReserve implements RpcCommandHandler<ReserveCommand, ReserveResult>, MapperDto {

  private final ActionRepositoryEx actionService;

  @Override
  public ReserveResult apply(ReserveCommand cmd) {
    var whenProvided = cmd.getOptionalWhenCase() == OptionalWhenCase.WHEN
        ? fromDto(cmd.getWhen())
        : LocalDate.now();
    var emailOfCurrentUser = cmd.getInvoker().getRequestorEmail();
    var projectId = UUID.fromString(cmd.getInvoker().getProjectId());
    var model = new ActionValue()
        .setWho(ValEmail.of(emailOfCurrentUser))
        .setWhen(whenProvided);
    var entityId = ShardedId.anyNew(projectId);
    actionService.save(entityId, model);
    var result = toDto(entityId);
    return ReserveResult.newBuilder()
        .setEntityId(result)
        .build();
  }

}
