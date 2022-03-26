package sinnet.gql.api;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Source;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.Transform;
import sinnet.gql.models.CustomerEntity;
import sinnet.gql.models.CustomersQuery;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.customers.Customers;
import sinnet.grpc.customers.GetRequest;

@GraphQLApi
@Slf4j
public class CustomersQueryGet implements CustomerMapper {

  @GrpcClient("activities")
  Customers service;

  public Uni<CustomerEntity> get(@Source CustomersQuery self, @Id String entityId) {
    var request = GetRequest.newBuilder()
        .setEntityId(EntityId.newBuilder()
          .setProjectId(self.getProjectId())
          .setEntityId(entityId))
          .build();
    return service.get(request)
        .onItemOrFailure()
        .transform(Transform.logged(log, it -> {
          return this.toGql(it);
        }));
        
  }

}
