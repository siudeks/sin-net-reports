package sinnet.gql.api;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Source;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.ServiceModel;
import sinnet.gql.Transform;
import sinnet.gql.models.CustomerEntity;
import sinnet.grpc.GrpcCustomers;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.customers.GetRequest;

@GraphQLApi
@ApplicationScoped
@Slf4j
public class ActionsQueryGetServiceModel implements CustomerMapper {

  @Inject GrpcCustomers service;

  public Uni<CustomerEntity> getCustomer(@Source ServiceModel self) {
    
    var customerId = self.getCustomerId();
    if (StringUtils.isEmpty(customerId)) return Uni.createFrom().nullItem();

    var entity = EntityId.newBuilder()
        .setProjectId(self.getProjectId())
        .setEntityId(customerId)
        .setEntityVersion(0);

    var request = GetRequest.newBuilder()
        .setEntityId(entity)
        .setUserToken(self.getUserToken())
        .build();

        return service.get(request)
        .onItemOrFailure()
        .transform(Transform.logged(log, it -> toGql(it)));
  }
}
