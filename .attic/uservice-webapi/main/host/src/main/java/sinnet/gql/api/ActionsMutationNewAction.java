package sinnet.gql.api;

import java.time.LocalDate;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import sinnet.gql.models.ActionsMutation;
import sinnet.gql.models.Entity;
import sinnet.gql.models.Mapper;
import sinnet.grpc.GrpcTimeEntries;
import sinnet.grpc.timeentries.ReserveCommand;

@GraphQLApi
public class ActionsMutationNewAction implements Mapper {

  @Inject GrpcTimeEntries service;

  public @NonNull Uni<Entity> newAction(@Source ActionsMutation self, @NonNull LocalDate whenProvided) {
    var cmd = ReserveCommand.newBuilder()
        .setInvoker(self.getUserToken())
        .setWhen(toGrpc(whenProvided))
        .build();
    return service
      .reserve(cmd)
      .map(this::toGql);
  }
}
