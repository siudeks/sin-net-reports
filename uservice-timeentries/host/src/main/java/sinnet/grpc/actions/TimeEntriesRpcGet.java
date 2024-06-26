package sinnet.grpc.actions;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.grpc.mapping.RpcQueryHandler;
import sinnet.grpc.timeentries.GetQuery;
import sinnet.grpc.timeentries.GetReply;
import sinnet.write.ActionRepositoryEx;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class TimeEntriesRpcGet implements RpcQueryHandler<GetQuery, GetReply> {

  private final ActionRepositoryEx actionRepository;

  @Override
  public GetReply apply(GetQuery request) {
    var projectId = UUID.fromString(request.getProjectId());
    var itemId = UUID.fromString(request.getTimeentryId());
    var item = actionRepository.get(projectId, itemId);
    var response = MapperDto.toDto(item);
    return GetReply.newBuilder()
        .setItem(response)
        .build();
  }

}
