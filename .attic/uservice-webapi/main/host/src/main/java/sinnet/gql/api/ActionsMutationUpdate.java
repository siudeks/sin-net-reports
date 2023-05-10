package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.TimeentriesMapper;
import sinnet.gql.Transform;
import sinnet.gql.models.ActionsMutation;
import sinnet.gql.models.ServiceEntry;
import sinnet.grpc.GrpcTimeEntries;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.timeentries.UpdateCommand;

@GraphQLApi
@Slf4j
public class ActionsMutationUpdate implements TimeentriesMapper {

  @Inject GrpcTimeEntries service;
  
  public Uni<@NonNull Boolean> update(@Source ActionsMutation self,
                        @NonNull @Id String entityId,
                        int entityVersion,
                        ServiceEntry content) {

    var eid = EntityId.newBuilder()
        .setProjectId(self.getProjectId())
        .setEntityId(entityId)
        .setEntityVersion(entityVersion)
        .build();
    var cmd = UpdateCommand.newBuilder()
        .setModel(toGrpc(eid, content))
        .build();
    return service.update(cmd)
        .onItemOrFailure()
        .transform(Transform.logged(log, it -> {
          return it.getSuccess();
        }));
  }
}
