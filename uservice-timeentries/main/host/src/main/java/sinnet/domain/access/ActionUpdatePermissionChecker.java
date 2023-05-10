package sinnet.domain.access;

import io.vavr.collection.Seq;
import lombok.Value;
import sinnet.domain.model.ValProjectId;

@Value
final class ActionUpdatePermissionChecker implements ActionPermissionChecker {

  private final ValProjectId projectId;

  @Override
  public ValidationResult onOwnerRole(Seq<ValProjectId> projectsId) {
    return projectsId.contains(projectId)
        ? ValidationResult.PERMITTED
        : ValidationResult.IGNORED;
  }

  @Override
  public ValidationResult onOperatorRole(Seq<ValProjectId> projectsId) {
    return projectsId.contains(projectId)
        ? ValidationResult.PERMITTED
        : ValidationResult.IGNORED;
  }

}
