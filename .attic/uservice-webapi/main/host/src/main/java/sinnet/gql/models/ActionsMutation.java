package sinnet.gql.models;

import org.eclipse.microprofile.graphql.Ignore;

import lombok.Value;
import sinnet.grpc.common.UserToken;

@Value
public class ActionsMutation {
  private String projectId;
  @Ignore
  private UserToken userToken;
}
