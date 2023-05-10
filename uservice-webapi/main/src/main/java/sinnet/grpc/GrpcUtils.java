package sinnet.grpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import lombok.val;
import lombok.experimental.UtilityClass;

/** Some static helper methods around gRpc. Candidate to be moved to shared library. */
@UtilityClass
public class GrpcUtils {
  
  private final Metadata.Key<String> daprHeaderKey = Metadata.Key.of("dapr-app-id", Metadata.ASCII_STRING_MARSHALLER);
  
  /** gRpc interceptor to add to each incoming call header with given {@code targetDaprApplicationId}. */
  public static ClientInterceptor addTargetDaprApplicationId(String targetDaprApplicationId) {
    var extraMetadata = new Metadata();
    extraMetadata.put(daprHeaderKey, targetDaprApplicationId);
    return MetadataUtils.newAttachHeadersInterceptor(extraMetadata);
  }

  // packs non closeable item to AutoCloseable element
  static AutoCloseable asCloseable(ManagedChannel item) {
    val someNotTestedShutdownForChannelsInSeconds = 3;
    return () -> {
      item.shutdown();
      try {
        item.awaitTermination(someNotTestedShutdownForChannelsInSeconds, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    };
  }
  
}
