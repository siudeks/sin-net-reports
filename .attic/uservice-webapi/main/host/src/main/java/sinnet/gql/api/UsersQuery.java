package sinnet.gql.api;

import org.eclipse.microprofile.graphql.Ignore;

import lombok.Value;
import sinnet.grpc.common.UserToken;

@Value
public class UsersQuery {
  private String projectId;
  @Ignore
  private UserToken userToken;
}
