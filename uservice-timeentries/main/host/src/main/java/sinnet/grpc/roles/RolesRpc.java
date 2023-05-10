package sinnet.grpc.roles;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import sinnet.grpc.roles.GetReply;
import sinnet.grpc.roles.GetRequest;
import sinnet.grpc.roles.RbacGrpc.RbacImplBase;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class RolesRpc extends RbacImplBase {
  
  private final RolesRpcGet get;

  @Override
  public void get(GetRequest request, StreamObserver<GetReply> responseObserver) {
    get.query(request, responseObserver);
  }
}
