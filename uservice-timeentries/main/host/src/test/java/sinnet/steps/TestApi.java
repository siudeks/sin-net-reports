package sinnet.steps;

import static sinnet.grpc.timeentries.ReserveCommand.newBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import sinnet.events.AvroObjectSerializer;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.projects.generated.CreateRequest;
import sinnet.grpc.projects.generated.ProjectIdOrBuilder;
import sinnet.grpc.projects.generated.UpdateCommand;
import sinnet.grpc.timeentries.LocalDate;
import sinnet.grpc.timeentries.SearchQuery;
import sinnet.grpc.users.IncludeOperatorCommand;
import sinnet.models.ProjectId;
import sinnet.models.ValEmail;
import sinnet.models.ValName;

@RequiredArgsConstructor
@Component
public class TestApi {

  private final RpcApi rpcApi;

  @SneakyThrows
  void createNewProject(ClientContext ctx, ValName projectAlias) {
    var invoker = sinnet.grpc.projects.generated.UserToken.newBuilder()
        .setRequestorEmail(ctx.currentOperator.getValue())
        .build();
    var cmd = CreateRequest.newBuilder()
        .setUserToken(invoker)
        .build();
    var reply = rpcApi.getProjects().create(cmd);
    var projectId = ProjectId.of(reply.getEntityId().getEId(), reply.getEntityId().getETag());

    ctx.setCurrentProject(projectAlias, projectId);
  }

  @SneakyThrows
  void updateProject(ClientContext ctx, ValName projectAlias, String newName) {
    var userToken = toGrpc(ctx.currentOperator);
    var projectIdAsProto = toGrpc(ctx.getProjectId(projectAlias));
    var cmd = UpdateCommand.newBuilder()
        .setUserToken(userToken)
        .setEntityId(projectIdAsProto)
        .build();

    var result = rpcApi.getProjects().update(cmd);
    Assertions.assertThat(result.getEntityId().getETag()).isGreaterThan(0);

    var projectId = ProjectId.of(result.getEntityId().getEId(), result.getEntityId().getETag());
    ctx.setCurrentProject(projectAlias, projectId);
  }

  void addOperator(ClientContext ctx) {
    var projectAlias = ctx.currentProject();
    var operatorAlias = ctx.currentOperator();
    var projectId = ctx.getProjectId(projectAlias);
    var operatorId = ctx.getOperatorId(operatorAlias, false);
    var cmd = IncludeOperatorCommand.newBuilder()
        .setProjectId(projectId.getId().toString())
        .addOperatorEmail(operatorId.getValue())
        .build();
    rpcApi.getUsers().includeOperator(cmd);
  }

  void createEntry(ClientContext ctx) {
    var projectAlias = ctx.currentProject();
    var operatorAlias = ctx.currentOperator();
    var projectId = ctx.getProjectId(projectAlias);
    var operatorId = ctx.getOperatorId(operatorAlias, false);
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
    var projectId = ctx.getProjectId(projectAlias);
    var result = rpcApi.getTimeentries().search(SearchQuery.newBuilder()
        .setFrom(singleDay)
        .setTo(singleDay)
        .setProjectId(projectId.getId().toString())
        .build());
    return result.getActivitiesList().stream().map(it -> it.getEntityId()).toList();
  }

  static sinnet.grpc.projects.generated.ProjectId toGrpc(ProjectId projectId) {
    return sinnet.grpc.projects.generated.ProjectId.newBuilder()
      .setEId(projectId.getId())
      .setETag(projectId.getVersion())
      .build();
  }

  static sinnet.grpc.projects.generated.UserToken toGrpc(ValName currentOperator) {
    return sinnet.grpc.projects.generated.UserToken.newBuilder()
      .setRequestorEmail(currentOperator.getValue())
      .build();
  }

}

/** Represents single endpoin connection, where someone logegd in and will be the same till the end of the session. */
@Accessors(fluent = true)
class ClientContext {
  @Getter
  ValName currentOperator;
  @Getter
  ValName currentProject;
  @Getter
  EntityId latestTimeentryId;

  @Getter
  LocalDate todayAsDto = LocalDate.newBuilder().setMonth(1).setDay(1).setYear(2020).build();

  @Getter
  private final KnownFacts known = new KnownFacts();

  public void setCurrentProject(@NonNull ValName projectAlias, ProjectId projectId) {
    currentProject = projectAlias;
    known.projects().put(projectAlias, projectId);
  }

  public void useCurrentProject(@NonNull ValName projectAlias) {
    currentProject = projectAlias;
  }

  public void newTimeentry(EntityId id, LocalDate when) {
    var entry = new TimeentryContext(when);
    known().timeentries.put(id, entry);
    latestTimeentryId = id;
  }

  public ProjectId getProjectId(@NonNull ValName projectAlias) {
    return known.projects().get(projectAlias);
  }

  public ValEmail getOperatorId(@NonNull ValName operatorAlias, boolean setCurrent) {
    if (setCurrent)
      currentOperator = operatorAlias;
    var actual = known.users.get(operatorAlias);
    if (actual != null)
      return actual;
    var emailAsString = "user@" + UUID.randomUUID();
    var email = ValEmail.of(emailAsString);
    known.users().put(operatorAlias, email);
    return email;
  }

  @Getter
  @Accessors(fluent = true, chain = false)
  class KnownFacts {
    private final Map<ValName, ValEmail> users = new HashMap<>();
    private final Map<ValName, ProjectId> projects = new HashMap<>();
    private final Map<EntityId, TimeentryContext> timeentries = new HashMap<>();
  }

}

record TimeentryContext(LocalDate when) {
}
