package sinnet.grpc.customers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import sinnet.grpc.common.EntityId;

/**
 * TBD.
 */
@Component
public class CustomersReserveProvider {

  void query(ReserveRequest request, StreamObserver<ReserveReply> observer) {
    var projectId = UUID.fromString(request.getProjectId());
    var result = sinnet.models.ShardedId.anyNew(projectId);
    var reply = ReserveReply.newBuilder()
        .setEntityId(EntityId.newBuilder()
            .setProjectId(result.getProjectId().toString())
            .setEntityId(result.getId().toString())
            .setEntityVersion(result.getVersion())
            .build())
        .build();
    observer.onNext(reply);
    observer.onCompleted();
  }
}

