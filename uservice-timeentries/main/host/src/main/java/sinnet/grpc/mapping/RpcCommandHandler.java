package sinnet.grpc.mapping;

import com.google.protobuf.GeneratedMessageV3;

import io.grpc.stub.StreamObserver;

/**
 * TBD.
 */
public interface RpcCommandHandler<Q extends GeneratedMessageV3, S extends GeneratedMessageV3> {

  /**
   * TBD.
   */
  S apply(Q cmd);

  /**
   * TBD.
   */
  default void command(Q request, StreamObserver<S> responseStream) {
    S result = this.apply(request);
    responseStream.onNext(result);
    responseStream.onCompleted();
  }
}
