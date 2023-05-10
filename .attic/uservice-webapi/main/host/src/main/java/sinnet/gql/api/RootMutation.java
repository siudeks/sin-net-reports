package sinnet.gql.api;

import javax.inject.Inject;
import javax.json.JsonArray;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.mutiny.Uni;
import sinnet.gql.models.ActionsMutation;
import sinnet.gql.models.ProjectsMutation;
import sinnet.gql.security.AccessProvider;
import sinnet.grpc.GrpcRbac;
import sinnet.grpc.projects.UserToken;

@GraphQLApi
public class RootMutation {

  @Inject GrpcRbac projectsGrpc;

  @Inject
  AccessProvider accessProvider;

  @Name("Customers")
  @Mutation
  public @NonNull Uni<CustomersMutation> customers(@NonNull @Id String projectId) {
    return accessProvider.with(projectId)
        .map(it -> new CustomersMutation(projectId, it.getUserToken()));
  }

  @Name("Actions")
  @Mutation
  public @NonNull Uni<ActionsMutation> actions(@NonNull @Id String projectId) {
    return accessProvider.with(projectId)
        .map(it -> new ActionsMutation(projectId, it.getUserToken()));
  }

  @Inject
  JsonWebToken jwt;
  
  @Name("Projects")
  @Mutation
  public @NonNull ProjectsMutation projects() {
    var emailsAsObject = jwt.getClaim("emails");
    var emails = (JsonArray) emailsAsObject;
    var firstEmail = emails.getString(0);
    
    var userToken = UserToken.newBuilder()
        // .setProjectId(null)
        .setRequestorEmail(firstEmail)
        .build();

    return new ProjectsMutation(userToken);
  }
}
