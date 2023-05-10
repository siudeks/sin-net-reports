package sinnet.rpc.conf;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

/**
 * Inspired by https://sultanov.dev/blog/exception-handling-in-grpc-java-server/
 */
public class ExceptionHandler implements ServerInterceptor {

  /**
   * {@inheritDoc}
   * 
   */
  @Override
  public <Q, S> ServerCall.Listener<Q> interceptCall(ServerCall<Q, S> serverCall, Metadata metadata,
      ServerCallHandler<Q, S> serverCallHandler) {
    ServerCall.Listener<Q> listener = serverCallHandler.startCall(serverCall, metadata);
    return new ExceptionHandlingServerCallListener<>(listener, serverCall, metadata);
  }

  private class ExceptionHandlingServerCallListener<ReqT, RespT>
      extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {
    private ServerCall<ReqT, RespT> serverCall;
    private Metadata metadata;

    ExceptionHandlingServerCallListener(ServerCall.Listener<ReqT> listener, ServerCall<ReqT, RespT> serverCall,
        Metadata metadata) {
      super(listener);
      this.serverCall = serverCall;
      this.metadata = metadata;
    }

    @Override
    public void onHalfClose() {
      try {
        super.onHalfClose();
      } catch (RuntimeException ex) {
        handleException(ex, serverCall, metadata);
        throw ex;
      }
    }

    @Override
    public void onReady() {
      try {
        super.onReady();
      } catch (RuntimeException ex) {
        handleException(ex, serverCall, metadata);
        throw ex;
      }
    }

    private void handleException(RuntimeException exception, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
      if (exception instanceof IllegalArgumentException) {
        serverCall.close(Status.INVALID_ARGUMENT.withDescription(exception.getMessage()), metadata);
      } else {
        serverCall.close(Status.UNKNOWN, metadata);
      }
    }
  }
}
