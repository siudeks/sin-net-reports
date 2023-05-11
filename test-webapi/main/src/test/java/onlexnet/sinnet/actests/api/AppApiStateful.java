package onlexnet.sinnet.actests.api;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;

import io.cucumber.java.PendingException;
import lombok.val;
import lombok.experimental.Delegate;
import onlexnet.sinnet.actests.api.AppApiMutation.ProjectId;
import onlexnet.sinnet.actests.api.AppApiMutation.SaveProjectResult;
import onlexnet.sinnet.actests.api.SessionState.ProjectModel;
import onlexnet.sinnet.webapi.test.AppQuery;
import onlexnet.sinnet.webapi.test.Jwt;

/**
 * Exposes read operations, and controls write operations with result of context
 * os write operations.
 */
public class AppApiStateful {

  @Delegate(types = AppApiQuery.class)
  private final AppQuery appApi;
  private final SessionState state;

  @Value("${sinnetapp.host}")
  String sinnetappHost;

  public AppApiStateful(SessionState state) {
    appApi = createAppApi(state.getUserEmail());
    this.state = state;
  }

  AppQuery createAppApi(String userEmail) {
    return new AppQuery(sinnetappHost, userEmail);
  }

  public SaveProjectResult createProject(String projectAlias) {
    val randomSuffix = RandomStringUtils.randomAlphabetic(6);
    var projectUniqueName = String.format("%s [%s]", projectAlias, randomSuffix);

    // var result = appApi.saveProject(projectUniqueName);
    // var id = result.save;
    // state.on(new ProjectCreated(id, projectAlias));
    // return result;
    throw new PendingException();
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
