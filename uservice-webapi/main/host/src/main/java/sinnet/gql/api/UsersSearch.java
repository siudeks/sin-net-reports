package sinnet.gql.api;


import java.util.List;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import io.vavr.collection.Iterator;
import lombok.RequiredArgsConstructor;
import sinnet.gql.models.UserGql;
import sinnet.grpc.UsersGrpcService;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.users.SearchRequest;

@Controller
@RequiredArgsConstructor
class UsersSearch {

  private final UsersGrpcService service;

  @SchemaMapping
  List<UserGql> search(UsersQuery self) {
    var request = SearchRequest.newBuilder()
        .setUserToken(UserToken.newBuilder()
            .setRequestorEmail(self.primaryEmail())
            .setProjectId(self.projectId()))
        .build();
    var items = service.search(request).getItemsList();
    return Iterator.ofAll(items)
        .map(it -> new UserGql(it.getEmail(), it.getEntityId()))
        .toJavaList();
  }

}

