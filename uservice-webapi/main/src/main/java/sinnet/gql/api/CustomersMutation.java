package sinnet.gql.api;

import javax.annotation.Untainted;

import lombok.Value;
import sinnet.grpc.common.UserToken;

/** Fixme. */
@Value
public class CustomersMutation {
  private String projectId;
  @Untainted 
  private UserToken userToken;
}
