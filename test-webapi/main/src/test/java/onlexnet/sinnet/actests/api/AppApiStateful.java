package onlexnet.sinnet.actests.api;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.val;
import lombok.experimental.Delegate;
import onlexnet.sinnet.actests.api.AppApiMutation.ProjectId;
import onlexnet.sinnet.actests.api.SessionState.ProjectModel;
import onlexnet.sinnet.webapi.test.AppQuery;
import sinnet.gql.models.ProjectEntityGql;

/**
 * Exposes read operations, and controls write operations with result of context
 * os write operations.
 */
public class AppApiStateful {

  @Delegate(types = AppApiQuery.class)
  private final AppQuery appApi;

  private final SessionState state;

  public AppApiStateful(String sinnetappHost, SessionState state) {
    appApi = createAppApi(sinnetappHost, state.getUserEmail());
    this.state = state;
  }

  AppQuery createAppApi(String host, String userEmail) {
    return new AppQuery(host, userEmail);
  }

  public ProjectEntityGql createProject(String projectAlias) {
    val randomSuffix = RandomStringUtils.randomAlphabetic(6);
    var projectUniqueName = String.format("%s [%s]", projectAlias, randomSuffix);

    var result = appApi.createProject(projectUniqueName);
    var model = result.get();
    state.on(new ProjectCreated(model, projectAlias));
    return model;
  }

  public void removeProject(ProjectId projectId) {
    // var result = appApi.removeProject(projectId);
    // state.on(new ProjectRemoved(projectId));
  }

  public void createTimeentry(String projectId) {
    // var now = LocalDate.now();
    // appApi.newAction(projectId, now);
    // state.on(new TimeentryCreated());
  }

  public void assignOperator(ProjectModel projectId, String operatorEmail) {
    // var eid = projectId.entity().getEntity().entityId;
    // var etag = projectId.entity().getEntity().entityVersion;
    // appApi.assignOperator(eid, etag, operatorEmail);
    // state.on(new OperatorAssigned());
  }

}
