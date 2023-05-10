package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.ServiceModel;
import sinnet.gql.TimeentriesMapper;
import sinnet.gql.Transform;
import sinnet.gql.models.ActionsQuery;
import sinnet.gql.models.ServiceFilter;
import sinnet.gql.models.ServicesSearchResult;
import sinnet.grpc.GrpcTimeEntries;
import sinnet.grpc.timeentries.SearchQuery;

@GraphQLApi
@Slf4j
public class ActionsQuerySearch implements TimeentriesMapper {

  @Inject GrpcTimeEntries service;

  public @NonNull Uni<@NonNull ServicesSearchResult> search(@Source ActionsQuery self, ServiceFilter filter) {
    var query = SearchQuery.newBuilder()
        .setProjectId(self.getProjectId())
        .setFrom(toGrpc(filter.getFrom()))
        .setTo(toGrpc(filter.getTo()))
        .build();
    return service.search(query)
      .onItemOrFailure()
      .transform(Transform.logged(log, it -> List
        .ofAll(it.getActivitiesList())
        .map(o -> {
          var item = new ServiceModel();
          item.setProjectId(o.getEntityId().getProjectId());
          item.setEntityId(o.getEntityId().getEntityId());
          item.setEntityVersion(o.getEntityId().getEntityVersion());
          item.setServicemanEmail(o.getServicemanEmail());
          item.setServicemanName(o.getServicemanName());
          item.setWhenProvided(toGql(o.getWhenProvided()));
          item.setDescription(o.getDescription());
          item.setDistance(o.getDistance());
          item.setDuration(o.getDuration());
          item.setCustomerId(o.getCustomerId());
          item.setUserToken(self.getUserToken());
          return item;
        }).toJavaArray(ServiceModel[]::new)
      ))
      .map(it -> {
          var result = new ServicesSearchResult();
          result.setItems(it);
          return result;
      });
  }
}
