package sinnet.gql.api;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.mutiny.Uni;
import sinnet.gql.models.ActionsQuery;
import sinnet.gql.models.CustomersQuery;
import sinnet.gql.models.PrincipalModel;
import sinnet.gql.models.ProjectsQuery;
import sinnet.gql.security.AccessProvider;
import sinnet.grpc.common.UserToken;

@GraphQLApi
public class RootQuery {

  @Inject
  JsonWebToken jwt;
  
  @Inject
  AccessProvider accessProvider;

  @Query("getPrincipal")
  public @NonNull PrincipalModel getPrincipal() {
    return new PrincipalModel();
  }

  @Context SecurityContext ctx;
  
  @Query("test")
  public @NonNull PrincipalModel test() {
    var debugName = ctx.getAuthenticationScheme();
    var result = new PrincipalModel();
    result.setName(debugName);
    return result;
  }

  @Query("Projects")
  public @NonNull sinnet.gql.models.ProjectsQuery projects() {
    var emailsAsObject = jwt.getClaim("emails");
    var emails = (JsonArray) emailsAsObject;
    var firstEmail = emails.getString(0);
    
    var userToken = UserToken.newBuilder()
        // .setProjectId(null)
        .setRequestorEmail(firstEmail)
        .build();

    return new ProjectsQuery(userToken);
  }

  @Query("Customers")
  public @NonNull Uni<sinnet.gql.models.CustomersQuery> customers(@NonNull @Id String projectId) {
    return accessProvider.with(projectId)
        .map(it -> new CustomersQuery(projectId, it.getUserToken()));
  }

  @Query("Actions")
  public @NonNull Uni<ActionsQuery> actions(@NonNull @Id String projectId) {
    return accessProvider.with(projectId)
        .map(it -> new ActionsQuery(projectId, it.getUserToken()));
  }

  @Query("Users")
  public @NonNull Uni<sinnet.gql.api.UsersQuery> users(@NonNull @Id String projectId) {
    return accessProvider.with(projectId)
        .map(it -> new UsersQuery(projectId, it.getUserToken()));
  }


}
