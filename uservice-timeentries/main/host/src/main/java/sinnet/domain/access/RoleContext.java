package sinnet.domain.access;

import io.vavr.collection.HashSet;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import sinnet.domain.access.ActionPermissionChecker.ValidationResult;
import sinnet.domain.model.ValProjectId;

/** Holder for all user context sets. When created keeps roles of an User. */
@Value
@RequiredArgsConstructor
public class RoleContext {

  private final Seq<RoleContextSet> contextSets;

  public Boolean canCreateProject(ValProjectId projectId) {
    var validator = new ActionCreatePermissionChecker(projectId);
    return collect(validator, contextSets);
  }

  public Boolean canDeleteProject(ValProjectId projectId) {
    var validator = new ActionDeletePermissionChecker(projectId);
    return collect(validator, contextSets);
  }

  public Boolean canUpdateProject(ValProjectId projectId) {
    var validator = new ActionUpdatePermissionChecker(projectId);
    return collect(validator, contextSets);
  }

  private static Boolean collect(ActionPermissionChecker checker, Seq<RoleContextSet> contextSets) {
    var setOfValidation = contextSets.map(it -> it.processPermission(checker)).collect(HashSet.collector());
    return toBoolean(setOfValidation);
  }

  private static Boolean toBoolean(Set<ValidationResult> asSet) {
    if (asSet.contains(ValidationResult.VETOED)) {
      return false;
    }
    if (asSet.contains(ValidationResult.PERMITTED)) {
      return true;
    }
    return false;
  }

}

