package sinnet.domain.access;

import io.vavr.collection.Seq;
import lombok.Value;
import lombok.experimental.Accessors;
import sinnet.domain.access.ActionPermissionChecker.ValidationResult;
import sinnet.domain.model.ValProjectId;

/** 
 * Marker interface for all known user roles set.
 *
 *<p>The set is calculated for all resources permitted for some UserToken.
 */
public sealed interface RoleContextSet {

  /** Invokes proper method based on user's role, and returns the result of validation. */
  ValidationResult processPermission(ActionPermissionChecker validator);

  /** Allows to check . */
  @Value
  @Accessors(fluent = true)
  class NoRoleContext implements RoleContextSet {

    @Override
    public ValidationResult processPermission(ActionPermissionChecker validator) {
      return ValidationResult.PERMITTED;
    }
  }
  
  /** Allows to check . */
  @Value
  @Accessors(fluent = true)
  class OwnerRoleContext implements RoleContextSet {
    private final Seq<ValProjectId> projectsIds;

    @Override
    public ValidationResult processPermission(ActionPermissionChecker validator) {
      return validator.onOwnerRole(projectsIds);
    }
  }
  
  /** List of project where the user has access as Operator. */
  @Value
  @Accessors(fluent = true)
  class OperatorRoleContext implements RoleContextSet {
    private final Seq<ValProjectId> projectsId;

    @Override
    public ValidationResult processPermission(ActionPermissionChecker validator) {
      return validator.onOperatorRole(projectsId);
    }
  }

}
