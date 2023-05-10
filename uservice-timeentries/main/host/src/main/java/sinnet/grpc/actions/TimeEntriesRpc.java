package sinnet.grpc.actions;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import sinnet.grpc.timeentries.GetQuery;
import sinnet.grpc.timeentries.GetReply;
import sinnet.grpc.timeentries.RemoveCommand;
import sinnet.grpc.timeentries.RemoveResult;
import sinnet.grpc.timeentries.ReserveCommand;
import sinnet.grpc.timeentries.ReserveResult;
import sinnet.grpc.timeentries.SearchQuery;
import sinnet.grpc.timeentries.SearchReply;
import sinnet.grpc.timeentries.UpdateCommand;
import sinnet.grpc.timeentries.UpdateResult;
import sinnet.grpc.timeentries.TimeEntriesGrpc.TimeEntriesImplBase;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class TimeEntriesRpc extends TimeEntriesImplBase {

  private final TimeEntriesRpcSearch search;
  private final TimeEntriesRpcGet get;
  private final TimeEntriesRpcReserve reserve;
  private final TimeEntriesRpcRemove remove;
  private final TimeEntriesRpcUpdate update;
    
  @Override
  public void search(SearchQuery query, StreamObserver<SearchReply> observer) {
    search.query(query, observer);
  }

  @Override
  public void get(GetQuery query, StreamObserver<GetReply> observer) {
    get.query(query, observer);
  }

  @Override
  public void reserve(ReserveCommand cmd, StreamObserver<ReserveResult> streamObserver) {
    reserve.command(cmd, streamObserver);
  }

  @Override
  public void remove(RemoveCommand request, StreamObserver<RemoveResult> responseObserver) {
    remove.command(request, responseObserver);
  }

  @Override
  public void update(UpdateCommand request, StreamObserver<UpdateResult> responseObserver) {
    update.command(request, responseObserver);
  }
}
