package sinnet.features;

import static sinnet.grpc.timeentries.ReserveCommand.newBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;

import io.dapr.v1.DaprAppCallbackProtos.TopicEventRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import sinnet.events.AvroObjectSerializer;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.timeentries.LocalDate;
import sinnet.grpc.timeentries.SearchQuery;
import sinnet.grpc.users.IncludeOperatorCommand;
import sinnet.models.ProjectId;
import sinnet.models.ValEmail;
import sinnet.models.ValName;
import sinnet.project.events.ProjectCreatedEvent;

@RequiredArgsConstructor
@Component
public class TestApi {

  private final RpcApi rpcApi;

  // we use the same deserializer as the whole ecosystem in ser / deser events.
  private final AvroObjectSerializer objectSerializer = new AvroObjectSerializer();

  @SneakyThrows
  void notifyNewProject(ClientContext ctx, ValName projectAlias) {
    var projectId = ctx.setProjectId(projectAlias);
    var event = ProjectCreatedEvent.newBuilder()
        .setEid(projectId.getId().toString())
        .setEtag(projectId.getVersion())
        .build();
    var eventSerialized = objectSerializer.serialize(event);
    var data = ByteString.copyFrom(eventSerialized);
    var te = TopicEventRequest.newBuilder().setData(data).build();
    rpcApi.getApiCallback().onTopicEvent(te);
  }

  void assignOperator(ClientContext ctx) {
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

}

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

  public ProjectId setProjectId(@NonNull ValName projectAlias) {
    currentProject = projectAlias;
    var projectId = ProjectId.anyNew();
    known.projects().put(projectAlias, projectId);
    return known.projects().get(projectAlias);
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
