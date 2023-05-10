package sinnet.grpc.actions;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.grpc.mapping.PropsBuilder;
import sinnet.grpc.mapping.RpcQueryHandler;
import sinnet.grpc.timeentries.SearchQuery;
import sinnet.grpc.timeentries.SearchReply;
import sinnet.grpc.timeentries.TimeEntryModel;
import sinnet.models.ActionValue;
import sinnet.models.Entity;
import sinnet.models.ValEmail;
import sinnet.write.ActionRepositoryEx;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class TimeEntriesRpcSearch implements RpcQueryHandler<SearchQuery, SearchReply>, MapperDto {

  private final ActionRepositoryEx repository;

  @Override
  public SearchReply apply(SearchQuery request) {
    var projectId = UUID.fromString(request.getProjectId());
    var from = fromDto(request.getFrom());
    var to = fromDto(request.getTo());
    var items = repository.list(projectId, from, to);
    var result = items.stream().map(this::map).toList();
    return SearchReply.newBuilder()
        .addAllActivities(result)
        .build();
  }

  private TimeEntryModel map(Entity<ActionValue> item) {
    return PropsBuilder.build(TimeEntryModel.newBuilder())
        .set(item.getId(), this::toDto, b -> b::setEntityId)
        .set(item.getValue(), ActionValue::getWhom, UUID::toString, b -> b::setCustomerId)
        .set(item.getValue(), o -> o.getWho().getValue(), b -> b::setServicemanEmail)
        .set(item.getValue(), ActionValue::getWho, ValEmail::getValue, b -> b::setServicemanName)
        .set(item.getValue(), ActionValue::getWhen, o -> toDto(o), b -> b::setWhenProvided)
        .set(item.getValue(), ActionValue::getWhat, b -> b::setDescription)
        .set(item.getValue(), o -> o.getHowLong().getValue(), b -> b::setDuration)
        .set(item.getValue(), o -> o.getHowFar().getValue(), b -> b::setDistance)
        .done().build();
  }

}
