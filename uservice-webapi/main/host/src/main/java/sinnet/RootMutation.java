package sinnet;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import sinnet.gql.CustomersMutation;
import sinnet.grpc.roles.Rbac;
import sinnet.security.AccessProvider;

@GraphQLApi
public class RootMutation {

  @GrpcClient("activities")
  Rbac projectsGrpc;

  @Inject
  AccessProvider accessProvider;

  @Name("Customers")
  @Mutation
  public @NonNull Uni<sinnet.gql.CustomersMutation> customers(@NonNull @Id String projectId) {
    return accessProvider.with(projectId)
        .map(it -> new CustomersMutation(projectId, it.getUserToken()));
  }

  @Name("Actions")
  @Mutation
  public @NonNull Uni<@NonNull ActionsMutation> actions(@NonNull @Id String projectId) {
    return accessProvider.with(projectId)
        .map(it -> new ActionsMutation(projectId, it.getUserToken()));
  }
}
