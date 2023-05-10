package sinnet.grpc.users;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import sinnet.grpc.users.UsersGrpc.UsersImplBase;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class UsersRpc extends UsersImplBase {

  private final UsersRpcSearch search;
  private final UsersRpcIncludeOperator includeOperator;

  @Override
  public void search(SearchRequest request, StreamObserver<SearchReply> responseObserver) {
    search.query(request, responseObserver);
  }

  @Override
  public void includeOperator(IncludeOperatorCommand request, StreamObserver<IncludeOperatorResult> responseObserver) {
    includeOperator.command(request, responseObserver);
  }
}
