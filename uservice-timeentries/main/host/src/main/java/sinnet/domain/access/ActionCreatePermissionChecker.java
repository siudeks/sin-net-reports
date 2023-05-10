package sinnet.domain.access;

import io.vavr.collection.Seq;
import lombok.Value;
import sinnet.domain.model.ValProjectId;

/** Checks if is allowed to create new project. */
@Value
final class ActionCreatePermissionChecker implements ActionPermissionChecker {

  private final ValProjectId projectId;

  /** To avoid creation of inifinitive number of projects, lets create some artificial limit. */
  @Override
  public ValidationResult onOwnerRole(Seq<ValProjectId> projectsId) {
    return ValidationResult.PERMITTED;
  }

  /** For creation new project beign Operator os some other projects doesn't matter. */
  @Override
  public ValidationResult onOperatorRole(Seq<ValProjectId> projectsId) {
    return ValidationResult.IGNORED;
  }

}
