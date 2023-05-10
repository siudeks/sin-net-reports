package sinnet.grpc.projects;

import com.google.protobuf.GeneratedMessageV3;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.vavr.control.Either;

/**
 * Designed to be extended by all Command rpc operations.
 *
 * @param <Q> - request type
 * @param <S> - response type
 */
public interface RpcCommandHandler<Q extends GeneratedMessageV3, S extends GeneratedMessageV3> {

  Either<Status, S> apply(Q cmd);

  /**
   * Executes the command and sends the result to the client.
   *
   * @param request       - the request
   * @param responseStream - the response stream
   */
  default void command(Q request, StreamObserver<S> responseStream) {
    var result = this.apply(request);
    if (result.isRight()) {
      responseStream.onNext(result.get());
    }
    if (result.isLeft()) {
      responseStream.onError(result.getLeft().asException());
    }
    responseStream.onCompleted();
  }
}
