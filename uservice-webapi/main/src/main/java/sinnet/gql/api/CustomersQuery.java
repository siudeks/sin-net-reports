package sinnet.gql.api;


import lombok.Value;
import sinnet.grpc.common.UserToken;

/** Fixme. */
@Value(staticConstructor = "of")
public class CustomersQuery {
  private String projectId;
  private UserToken userToken;
}
