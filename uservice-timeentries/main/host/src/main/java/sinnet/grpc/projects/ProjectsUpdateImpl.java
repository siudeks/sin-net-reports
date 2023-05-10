package sinnet.grpc.projects;

import static io.vavr.control.Either.right;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.grpc.Status;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import sinnet.domain.access.AccessFacade;
import sinnet.dbo.DboUpdate;
import sinnet.grpc.projects.generated.UpdateCommand;
import sinnet.grpc.projects.generated.UpdateResult;
import sinnet.domain.model.ValProjectId;

@Component
@RequiredArgsConstructor
class ProjectsUpdateImpl implements RpcCommandHandler<UpdateCommand, UpdateResult> {
  
  private final AccessFacade accessFacade;
  private final DboUpdate dboUpdate;

  @Override
  public Either<Status, UpdateResult> apply(UpdateCommand cmd) {
    var eidAsString = cmd.getEntityId().getEId();
    var eid = UUID.fromString(eidAsString);
    var idHolder = ValProjectId.of(eid);

    var requestor = cmd.getUserToken();

    accessFacade.guardAccess(requestor, idHolder, roleContext -> roleContext::canUpdateProject);
    return dboUpdate.update(cmd);
  }

  static <T> Either<Status, T> asEither(Optional<T> maybeValue) {
    if (maybeValue.isEmpty()) {
      return Either.left(Status.NOT_FOUND);
    }
    return Either.right(maybeValue.get());
  }
}
