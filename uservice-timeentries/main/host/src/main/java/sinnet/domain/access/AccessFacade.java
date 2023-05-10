package sinnet.domain.access;

import java.util.function.Function;
import java.util.function.Predicate;

import sinnet.grpc.projects.generated.UserToken;
import sinnet.domain.model.ValProjectId;

/** Entry to check if the user has access to requested action. */
public interface AccessFacade {

  /**
   * Checks if given user is entitled to make an action on given project.
   *
   * @throws StatusException if user is not entitled to make an action.
   */
  void guardAccess(UserToken requestor, ValProjectId eid, Function<RoleContext, Predicate<ValProjectId>> entitlementSelector);
}
