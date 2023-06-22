package sinnet.steps;

import static sinnet.grpc.timeentries.ReserveCommand.newBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
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
import sinnet.grpc.roles.GetRequest;
import sinnet.grpc.timeentries.LocalDate;
import sinnet.grpc.timeentries.SearchQuery;
import sinnet.grpc.users.IncludeOperatorCommand;
import sinnet.models.ProjectId;
import sinnet.models.ValEmail;
import sinnet.models.ValName;
import sinnet.steps.ClientContext.ProjectState;

@RequiredArgsConstructor
@Component
public class TestApi {

  private final RpcApi rpcApi;

  @SneakyThrows
  void createNewProject(ClientContext ctx, ValName projectAlias) {
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
  void updateProject(ClientContext ctx, ValName projectAlias, String newName) {
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

  void addOperator(ClientContext ctx, ValName operatorAlias, ValName projectAlias) {
    var appState = ctx.buildExpectedState();
    var operatorId = appState.user(operatorAlias).get();
    var projectId = appState.activeProject();
    var cmd = IncludeOperatorCommand.newBuilder()
        .setProjectId(projectId.getValue())
        .addOperatorEmail(operatorId.getValue())
        .build();
    rpcApi.getUsers().includeOperator(cmd);
  }

  int numberOfProjects(ClientContext ctx) {
    var appState = ctx.buildExpectedState();
    var operatorAlias = appState.currentUser();
    var operatorId = appState.user(operatorAlias).get();
    var qry = UserStatsRequest.newBuilder()
        .setEmailOfRequestor(operatorId.getValue())
        .build();
    var stats = rpcApi.getProjects().userStats(qry);
    return stats.getNumberOfProjects();
  }

  void createEntry(ClientContext ctx) {
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
    ctx.newTimeentry(returnedId, when);
  }

  List<EntityId> listTimeentries(ClientContext ctx, ValName projectAlias, LocalDate singleDay) {
    var facts = ctx.buildExpectedState();
    var projectId = facts.activeProject();
    var result = rpcApi.getTimeentries().search(SearchQuery.newBuilder()
        .setFrom(singleDay)
        .setTo(singleDay)
        .setProjectId(projectId.getValue())
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

/**
 * Represents single endpoin connection, where someone logged in and will be the
 * same till the end of the session.
 */
@Accessors(fluent = true)
class ClientContext {

  @Getter
  LocalDate todayAsDto = LocalDate.newBuilder().setMonth(1).setDay(1).setYear(2020).build();

  private Queue<AppEvent> appEvents = Queue.empty();

  sealed interface AppEvent {
  }

  record ProjectUpdated(@Nonnull ValName projectAlias, ValName projectName) implements AppEvent {
  }

  record ProjectCreated(@Nonnull ValName projectAlias, ProjectId projectId) implements AppEvent {
  }

  record TimeentryCreated(EntityId id, LocalDate when) implements AppEvent {
  }

  record ActiveUser(@Nonnull ValName userAlias, ValEmail email) implements AppEvent { }


  private void on(AppEvent appEvent) {
    appEvents = appEvents.append(appEvent);
  }

  public void newTimeentry(EntityId returnedId, LocalDate when) {
  }

  AppState buildExpectedState() {
    return appEvents.foldLeft(new AppState(), this::applyEvent);
  }

  AppState applyEvent(AppState facts, AppEvent event) {
    
    var users = facts.users();
    var projects = facts.projects();
    var timeentries = facts.timeentries();
    var currentUser = facts.currentUser();
    var activeProject = facts.activeProject();

    if (event instanceof ProjectUpdated e) {
      var state = facts.projects.get(e.projectAlias).get();
      var alias = e.projectAlias();
      state = new ProjectState(state.id(), e.projectName());
      projects = projects.put(alias, state);
    } else if (event instanceof ProjectCreated e) {
      var alias = e.projectAlias();
      var state = new ProjectState(e.projectId, null);
      projects = projects.put(alias, state);
      // to simplify tests: lastly creaated project is the current one
      activeProject = alias;
    } else if (event instanceof TimeentryCreated e) {
      var id = e.id();
      var state = new TimeentryState(e.when());
      timeentries = timeentries.put(id, state);
    } else if (event instanceof ActiveUser e) {
      var userAlias = e.userAlias;
      var userEmail = e.email;
      var maybeUser = ((Option<ValEmail>) users.get(userAlias)).toJavaOptional();
      if (maybeUser.isPresent()) {
        currentUser = userAlias;
      } else {
        users = users.put(userAlias, userEmail);
        currentUser = userAlias;
      }
    } else {
      throw new IllegalStateException();
    }
    return new AppState(users, projects, timeentries, currentUser, activeProject);
  }

  public void onUser(ValName userAlias, ValEmail email) {
    on(new ActiveUser(userAlias, email));
  }

  public void onProjectUpdated(@NonNull ValName projectAlias, ValName projectName) {
    on(new ProjectUpdated(projectAlias, projectName));
  }

  public void onProjectCreated(@NonNull ValName projectAlias, ProjectId projectId) {
    on(new ProjectCreated(projectAlias, projectId));
  }

  public void onTimeentryCreated(EntityId id, LocalDate when) {
    on(new TimeentryCreated(id, when));
  }

  record AppState(
    Map<ValName, ValEmail> users,
    Map<ValName, ProjectState> projects,
    Map<EntityId, TimeentryState> timeentries,
    ValName currentUser,
    ValName activeProject) {
    
    public AppState() {
      this(HashMap.empty(), HashMap.empty(), HashMap.empty(), ValName.empty(), ValName.empty());
    }

    public Optional<ProjectState> getProjectId(@NonNull ValName projectAlias) {
      return this.projects.get(projectAlias).toJavaOptional();
    }

    public Optional<ValEmail> user(ValName alias) {
      return this.users.get(alias).toJavaOptional();
    }

    public EntityId latestTimeentryId() {
      return this.timeentries.last()._1;
    }

    public TimeentryState latestTimeentry() {
      return this.timeentries.last()._2;
    }
  }
  
  record ProjectState(ProjectId id, ValName name) {}
  record TimeentryState(LocalDate when) {}


}
