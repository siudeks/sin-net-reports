package sinnet.gql.models;

import javax.annotation.Untainted;

import org.eclipse.microprofile.graphql.Ignore;

import lombok.Value;
import sinnet.grpc.projects.ProjectId;
import sinnet.grpc.projects.ProjectModel;
import sinnet.grpc.projects.UserToken;

/** Mutation context for a single project. */
@Value
public class ProjectMutation {
  @Untainted @Ignore ProjectId projectId;
  @Untainted @Ignore ProjectModel projectModel;
  @Untainted @Ignore UserToken userToken;
}
