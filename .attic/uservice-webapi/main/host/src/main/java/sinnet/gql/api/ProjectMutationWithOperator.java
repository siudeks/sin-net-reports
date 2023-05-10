package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import sinnet.gql.models.ProjectId;
import sinnet.gql.models.ProjectMutation;
import sinnet.grpc.GrpcProjects;
import sinnet.grpc.projects.UpdateCommand;
import sinnet.grpc.projects.UpdateResult;

@GraphQLApi
public class ProjectMutationWithOperator implements ProjectMapper {

  @Inject
  GrpcProjects service;

  public @NonNull Uni<@NonNull ProjectId> withOperator(@Source ProjectMutation self, @NonNull String operatorEmail) {
    var userToken = self.getUserToken();
    var projectId = self.getProjectId();
    var model = self.getProjectModel();
    model.toBuilder().addEmailOfOperator(operatorEmail);
    var cmd = UpdateCommand.newBuilder()
        .setDesired(model)
        .setEntityId(projectId)
        .setUserToken(userToken)
        .build();
    return service.update(cmd).map(UpdateResult::getEntityId).map(this::toGql);
  }
}
