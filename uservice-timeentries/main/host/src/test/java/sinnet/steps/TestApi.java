package sinnet.steps;

import static sinnet.grpc.timeentries.ReserveCommand.newBuilder;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import io.vavr.control.Option;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.projects.generated.CreateRequest;
import sinnet.grpc.projects.generated.ProjectModel;
import sinnet.grpc.projects.generated.UpdateCommand;
import sinnet.grpc.projects.generated.UserStatsRequest;
import sinnet.grpc.timeentries.LocalDate;
import sinnet.grpc.timeentries.SearchQuery;
import sinnet.grpc.users.IncludeOperatorCommand;
import sinnet.models.ProjectId;
import sinnet.models.ValEmail;
import sinnet.models.ValName;
import sinnet.steps.ExpectedState.ProjectState;

@RequiredArgsConstructor
@Component
public class TestApi {

  private final RpcApi rpcApi;

  @SneakyThrows
  void createNewProject(ExpectedState ctx, ValName projectAlias) {
    var appState = ctx.buildExpectedState();
    var userEmail = appState.user(appState.currentUser()).get();
    var invoker = sinnet.grpc.projects.generated.UserToken.newBuilder()
        .setRequestorEmail(userEmail.getValue())
        .build();
    var cmd = CreateRequest.newBuilder()
        .setUserToken(invoker)
        .build();
    var reply = rpcApi.getProjects().create(cmd);
    var projectId = ProjectId.of(reply.getEntityId().getEId(), reply.getEntityId().getETag());
    ctx.onProjectCreated(projectAlias, projectId);
  }

  @SneakyThrows
  void updateProject(ExpectedState ctx, ValName projectAlias, String newName) {
    var appState = ctx.buildExpectedState();
    var userAlias = appState.currentUser();
    var userEmail = appState.user(userAlias).get();
    var userToken = toGrpc(userEmail);
    var projectIdAsProto = toGrpc(appState.getProjectId(projectAlias).get().id());
    var cmd = UpdateCommand.newBuilder()
        .setUserToken(userToken)
        .setEntityId(projectIdAsProto)
        .setDesired(ProjectModel.newBuilder()
            .setName(newName)
            .setEmailOfOwner(userEmail.getValue()))
        .build();

    var result = rpcApi.getProjects().update(cmd);
    Assertions.assertThat(result.getEntityId().getETag()).isGreaterThan(0);
    ctx.onProjectUpdated(projectAlias, ValName.of(newName));
  }

  void addOperator(ExpectedState ctx, ValName operatorAlias, ValName projectAlias) {
    var appState = ctx.buildExpectedState();
    var operatorId = appState.user(operatorAlias).get();
    var projectId = appState.activeProject();
    var cmd = IncludeOperatorCommand.newBuilder()
        .setProjectId(projectId.getValue())
        .addOperatorEmail(operatorId.getValue())
        .build();
    rpcApi.getUsers().includeOperator(cmd);
  }

  int numberOfProjects(ExpectedState ctx) {
    var appState = ctx.buildExpectedState();
    var operatorAlias = appState.currentUser();
    var operatorId = appState.user(operatorAlias).get();
    var qry = UserStatsRequest.newBuilder()
        .setEmailOfRequestor(operatorId.getValue())
        .build();
    var stats = rpcApi.getProjects().userStats(qry);
    return stats.getNumberOfProjects();
  }

  void createEntry(ExpectedState ctx) {
    var appState = ctx.buildExpectedState();
    var projectAlias = appState.activeProject();
    var operatorAlias = appState.currentUser();
    var projectId = appState.getProjectId(projectAlias).get().id();
    var operatorId = appState.user(operatorAlias).get();

    var invoker = UserToken.newBuilder()
        .setProjectId(projectId.getId().toString())
        .setRequestorEmail(operatorId.getValue());
    var when = ctx.todayAsDto;
    var result = rpcApi.getTimeentries().reserve(newBuilder()
        .setInvoker(invoker)
        .setWhen(when)
        .build());
    var returnedId = result.getEntityId();
    ctx.onTimeentryCreated(returnedId, when);
  }

  List<EntityId> listTimeentries(ExpectedState ctx, ValName projectAlias, LocalDate singleDay) {
    var facts = ctx.buildExpectedState();
    var projectState = facts.getProjectId(projectAlias).get();
    var projectId = projectState.id();
    var result = rpcApi.getTimeentries().search(SearchQuery.newBuilder()
        .setFrom(singleDay)
        .setTo(singleDay)
        .setProjectId(projectId.getId())
        .build());
    return result.getActivitiesList().stream().map(it -> it.getEntityId()).toList();
  }

  static sinnet.grpc.projects.generated.ProjectId toGrpc(ProjectId projectId) {
    return sinnet.grpc.projects.generated.ProjectId.newBuilder()
        .setEId(projectId.getId())
        .setETag(projectId.getVersion())
        .build();
  }

  static ProjectId fromGrpc(sinnet.grpc.projects.generated.ProjectId projectId) {
    return new ProjectId(projectId.getEId(), projectId.getETag());
  }

  static sinnet.grpc.projects.generated.UserToken toGrpc(ValEmail currentOperator) {
    return sinnet.grpc.projects.generated.UserToken.newBuilder()
        .setRequestorEmail(currentOperator.getValue())
        .build();
  }

  public ProjectState getProject(ProjectId projectId) {
    var request = sinnet.grpc.projects.generated.GetRequest.newBuilder()
        .setProjectId(toGrpc(projectId))
        .build();
    var reply = rpcApi.getProjects().get(request);
    return new ProjectState(projectId, ValName.of(reply.getModel().getName()));
  }

}

