package sinnet.gql.security;

import lombok.Value;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.roles.GetReply.Role;

/** Single point to return verifed model of authorization of the current invoked based on JWT token.  */
public interface AccessProvider {

  
  WithResult with(String projectId);
  
  /** Helper class to keep operation result. */
  @Value(staticConstructor = "of")
  class WithResult {
    private UserToken userToken;
    private Role projectRole;
  }

}
