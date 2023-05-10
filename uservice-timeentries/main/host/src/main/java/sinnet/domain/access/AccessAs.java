package sinnet.domain.access;

import sinnet.grpc.projects.generated.UserToken;

/** Calculates for given {@code UserToken} what are (??).  */
interface AccessAs<T extends RoleContextSet> {
  T calculate(UserToken userToken);
}
