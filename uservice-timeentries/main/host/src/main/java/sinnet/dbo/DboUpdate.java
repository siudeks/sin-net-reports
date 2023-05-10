package sinnet.dbo;

import java.util.UUID;

import io.grpc.Status;
import io.vavr.control.Either;
import sinnet.domain.model.ProjectVid;
import sinnet.domain.model.ValEmail;
import sinnet.grpc.projects.generated.ProjectId;
import sinnet.grpc.projects.generated.UpdateCommand;
import sinnet.grpc.projects.generated.UpdateResult;

/** Support for update commands. */
public interface DboUpdate {

  /** Same as {@link #updateCommand(ProjectVid, UpdateCommandContent)} but using Dto types. */
  default Either<Status, UpdateResult> update(UpdateCommand request) {
    var eidAsString = request.getEntityId().getEId();
    var eidAsUuid = UUID.fromString(eidAsString);
    var etag = request.getEntityId().getETag();
    var vid = ProjectVid.of(eidAsUuid, etag);

    var desired = request.getDesired();
    var name = desired.getName();
    var emailOfOwner = ValEmail.of(desired.getEmailOfOwner());
    var operators = desired.getEmailOfOperatorList().stream().map(ValEmail::of).toArray(ValEmail[]::new);
    var content = new UpdateCommandContent(name, emailOfOwner, operators);

    return this.updateCommand(vid, content)
      .map(it -> UpdateResult.newBuilder()
            .setEntityId(ProjectId.newBuilder()
            .setEId(it.id().toString())
            .setETag(it.tag()))
          .build());
  }

  Either<Status, ProjectVid> updateCommand(ProjectVid vid, UpdateCommandContent content);

  /** Container for data used to update Project entity. */
  record UpdateCommandContent(String name, ValEmail newOwner, ValEmail... operators) {}
}
