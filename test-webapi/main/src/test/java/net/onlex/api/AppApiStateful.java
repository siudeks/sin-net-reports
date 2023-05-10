package net.onlex.api;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import io.smallrye.graphql.client.typesafe.api.TypesafeGraphQLClientBuilder;
import io.smallrye.jwt.build.Jwt;
import lombok.val;
import lombok.experimental.Delegate;
import net.onlex.api.AppApiMutation.ProjectId;
import net.onlex.api.AppApiMutation.SaveProjectResult;
import net.onlex.api.SessionState.ProjectModel;

/**
 * Exposes read operations, and controls write operations with result of context
 * os write operations.
 */
public class AppApiStateful {

  @Delegate(types = AppApiQuery.class)
  private final AppApi appApi;
  private final SessionState state;

  public AppApiStateful(SessionState state) {
    appApi = createAppApi(state.getUserEmail());
    this.state = state;
  }

  static AppApi createAppApi(String userEmail) {
    var secret = "my super secret key to sign my dev JWT token";
    var keyBytes = new String(Base64.getEncoder().encode(secret.getBytes()));
    var builder = Jwt.claims();
    var randomUsername = UUID.randomUUID().toString();
    var token = builder
        .issuer("https://issuer.org")
        .claim("emails", List.of(randomUsername + "@email.com"))
        .signWithSecret(keyBytes);
    var bearer = String.format("Bearer %s", token);
    return TypesafeGraphQLClientBuilder.newBuilder()
        .header("Authorization", bearer)
        .build(AppApi.class);

  }

  public SaveProjectResult createProject(String projectAlias) {
    val randomSuffix = RandomStringUtils.randomAlphabetic(6);
    var projectUniqueName = String.format("%s [%s]", projectAlias, randomSuffix);

    var result = appApi.saveProject(projectUniqueName);
    var id = result.save;
    state.on(new ProjectCreated(id, projectAlias));
    return result;
  }

  public void removeProject(ProjectId projectId) {
    var result = appApi.removeProject(projectId);
    state.on(new ProjectRemoved(projectId));
  }

  public void createTimeentry(String projectId) {
    var now = LocalDate.now();
    appApi.newAction(projectId, now);
    state.on(new TimeentryCreated());
  }

  public void assignOperator(ProjectModel projectId, String operatorEmail) {
    var eid = projectId.entity().getEntity().entityId;
    var etag = projectId.entity().getEntity().entityVersion;
    appApi.assignOperator(eid, etag, operatorEmail);
    state.on(new OperatorAssigned());
  }

}
