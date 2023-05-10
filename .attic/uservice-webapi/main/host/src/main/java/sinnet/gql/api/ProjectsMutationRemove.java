package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import sinnet.gql.models.ProjectId;
import sinnet.gql.models.ProjectsMutation;
import sinnet.grpc.GrpcProjects;
import sinnet.grpc.projects.RemoveCommand;

@GraphQLApi
public class ProjectsMutationRemove implements ProjectMapper {

  @Inject
  GrpcProjects service;

  public @NonNull Uni<Boolean> remove(@Source ProjectsMutation self, @NonNull ProjectId projectId) {
    var req = RemoveCommand.newBuilder()
        .setProjectId(toGrpc(projectId))
        .setUserToken(self.getUserToken())
        .build();
    return service.remove(req)
        .map(ignored -> true);
  }
}
