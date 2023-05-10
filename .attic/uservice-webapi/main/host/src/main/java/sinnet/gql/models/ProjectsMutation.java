package sinnet.gql.models;

import org.eclipse.microprofile.graphql.Ignore;

import lombok.Value;
import sinnet.grpc.projects.UserToken;

@Value
public class ProjectsMutation {
  private @Ignore UserToken userToken;
}
