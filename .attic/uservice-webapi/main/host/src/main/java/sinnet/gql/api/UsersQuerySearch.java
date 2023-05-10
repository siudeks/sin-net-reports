package sinnet.gql.api;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import io.vavr.collection.Iterator;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.Transform;
import sinnet.gql.models.User;
import sinnet.grpc.GrpcUsers;
import sinnet.grpc.users.SearchRequest;

@GraphQLApi
@Slf4j
public class UsersQuerySearch {

  @Inject GrpcUsers service;

  public @NonNull Uni<@NonNull User[]> search(@Source UsersQuery self) {
    var request = SearchRequest.newBuilder()
        .setUserToken(self.getUserToken())
        .build();
    return service.search(request)
        .onItemOrFailure()
        .transform(Transform.logged(log, it -> {
          return Iterator.ofAll(it.getItemsList()).map(o -> {
            var result = new User();
            result.setEntityId(o.getEntityId());
            result.setEmail(o.getEmail());
            return result;
          }).toJavaArray(User[]::new);
        }));
  }
}
