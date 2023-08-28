package sinnet.grpc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.gql.api.CommonMapper;
import sinnet.grpc.timeentries.SearchQuery;
import sinnet.grpc.timeentries.TimeEntriesGrpc.TimeEntriesBlockingStub;
import sinnet.grpc.timeentries.TimeEntryModel;


/** Mockable equivalent of {@link ProjectsGrpcStub}. */
@Component
@RequiredArgsConstructor
class ActionsGrpcFacadeImpl implements ActionsGrpcFacade {

  private final TimeEntriesBlockingStub stub;

  @Override
  public List<TimeEntryModel> searchInternal(UUID projectId, LocalDate from, LocalDate to) {

    var searchQuery = SearchQuery.newBuilder()
        .setFrom(CommonMapper.toGrpc(from))
        .setTo(CommonMapper.toGrpc(to))
        .setProjectId(projectId.toString())
        .build();

    var result = stub.search(searchQuery);

    return result.getActivitiesList();
  }

}
