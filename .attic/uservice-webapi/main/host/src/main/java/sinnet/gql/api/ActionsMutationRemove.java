package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.Transform;
import sinnet.gql.models.ActionsMutation;
import sinnet.grpc.GrpcTimeEntries;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.timeentries.RemoveCommand;
import sinnet.grpc.timeentries.RemoveResult;

@GraphQLApi
@Slf4j
public class ActionsMutationRemove {

  @Inject GrpcTimeEntries service;

  public Uni<@NonNull Boolean> remove(@Source ActionsMutation self, @NonNull @Id String entityId, int entityVersion) {
    var cmd = RemoveCommand.newBuilder()
        .setEntityId(EntityId.newBuilder()
          .setProjectId(self.getProjectId())
          .setEntityId(entityId)
          .setEntityVersion(entityVersion)
          .build())
        .build();
    return service.remove(cmd)
      .onItemOrFailure()
      .transform(Transform.logged(log, RemoveResult::getResult));
  }
}
