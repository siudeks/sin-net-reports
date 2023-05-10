package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import sinnet.gql.models.ProjectMutation;
import sinnet.gql.models.ProjectsMutation;
import sinnet.grpc.GrpcProjects;
import sinnet.grpc.projects.GetRequest;
import sinnet.grpc.projects.ProjectId;

@GraphQLApi
public class ProjectsMutationProject implements ProjectMapper {

  @Inject
  GrpcProjects service;

  public @NonNull Uni<ProjectMutation> project(@Source ProjectsMutation self, @NonNull @Id String eid, @NonNull @Id long etag) {
    var userToken = self.getUserToken();
    var projectId = ProjectId.newBuilder().setEId(eid).setETag(etag).build();
    var getRequest = GetRequest.newBuilder().setProjectId(projectId).build();
    return service.get(getRequest).map(it -> new ProjectMutation(projectId, it.getModel(), userToken));
  }
}
