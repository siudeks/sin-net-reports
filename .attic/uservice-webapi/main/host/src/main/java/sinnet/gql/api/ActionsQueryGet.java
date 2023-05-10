package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.ServiceModel;
import sinnet.gql.TimeentriesMapper;
import sinnet.gql.Transform;
import sinnet.gql.models.ActionsQuery;
import sinnet.grpc.GrpcTimeEntries;
import sinnet.grpc.timeentries.GetQuery;

@GraphQLApi
@Slf4j
public class ActionsQueryGet implements TimeentriesMapper {

  @Inject GrpcTimeEntries service;

  public Uni<ServiceModel> get(@Source ActionsQuery self, @Id String actionId) {
    var request = GetQuery.newBuilder()
        .setProjectId(self.getProjectId())
        .setTimeentryId(actionId)
        .build();
    return service.get(request)
        .onItemOrFailure()
        .transform(Transform.logged(log, it -> {
          return toGql(it, self.getUserToken());
        }));
  }
}
