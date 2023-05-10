package sinnet.grpc.projects;

import com.google.protobuf.GeneratedMessageV3;

import io.grpc.stub.StreamObserver;

/**
 * Designed to be extended by all Query rpc operations.
 *
 * @param <Q> - query type
 * @param <S> - response type
 */
public interface RpcQueryHandler<Q extends GeneratedMessageV3, S extends GeneratedMessageV3> {

  S apply(Q request);

  /**
   * Executes the query and sends the result to the client.
   *
   * @param request - the request
   * @param responseStream - the response stream
   */
  default void query(Q request, StreamObserver<S> responseStream) {
    S result = this.apply(request);
    responseStream.onNext(result);
    responseStream.onCompleted();
  }
}
