package sinnet.grpc.projects;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import io.grpc.Status;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import sinnet.domain.access.AccessFacade;
import sinnet.dbo.DboCreate;
import sinnet.dbo.DboFacade;
import sinnet.grpc.projects.generated.CreateReply;
import sinnet.grpc.projects.generated.CreateRequest;
import sinnet.grpc.projects.generated.ProjectId;
import sinnet.domain.model.ValEmail;
import sinnet.domain.model.ValProjectId;
import sinnet.project.events.ProjectCreatedEvent;

@Component
@RequiredArgsConstructor
class ProjectsCreateImpl implements RpcCommandHandler<CreateRequest, CreateReply> {

  private final AccessFacade accessFacade;
  private final DboFacade dboFacade;
  private final ApplicationEventPublisher onProjectCreated;

  @Override
  public Either<Status, CreateReply> apply(CreateRequest cmd) {
    var requestor = cmd.getUserToken();

    var valId = ValProjectId.of(UUID.randomUUID());
    var emailOfRequestor = ValEmail.of(requestor.getRequestorEmail());
    accessFacade.guardAccess(requestor, valId, roleContext -> roleContext::canCreateProject);

    var createResult = dboFacade.create(new DboCreate.CreateContent(valId, emailOfRequestor));
    if (createResult instanceof DboCreate.Success x) {
      var evt = ProjectCreatedEvent.newBuilder()
          .setEid(x.getVid().id().toString())
          .setEtag(x.getVid().tag())
          .build();
      onProjectCreated.publishEvent(evt);

      var result = CreateReply.newBuilder()
          .setEntityId(ProjectId.newBuilder()
              .setEId(valId.value().toString())
              .setETag(1))
          .build();
      return right(result);
    } else if (createResult instanceof DboCreate.InvalidContent x) {
      return left(Status.FAILED_PRECONDITION.withDescription(x.getReason()));
    } else if (createResult instanceof DboCreate.AboveLimits x) {
      return left(Status.RESOURCE_EXHAUSTED.withDescription(x.getReason()));
    } 
    return left(Status.INTERNAL);
  }

}
