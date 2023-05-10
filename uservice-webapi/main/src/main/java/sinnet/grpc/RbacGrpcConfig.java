package sinnet.grpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.ManagedChannelBuilder;
import sinnet.grpc.GrpcProperties.ServiceAddress;
import sinnet.grpc.roles.RbacGrpc;
import sinnet.grpc.roles.RbacGrpc.RbacBlockingStub;

@Configuration
class RbacGrpcConfig {

  @Bean
  Closeable.Of<RbacBlockingStub> disposableManagedChannel(GrpcProperties props) {
    return managedChannel(props.getRbac());
  }

  @Bean
  RbacBlockingStub managedChannel(Closeable.Of<RbacBlockingStub> managedCloseable) {
    return managedCloseable.item();
  }

  Closeable.Of<RbacBlockingStub> managedChannel(ServiceAddress address) {
    var daprAppId = address.getDaprAppId();
    var channel = ManagedChannelBuilder.forAddress(address.getHost(), address.getPort())
        .intercept(GrpcUtils.addTargetDaprApplicationId(daprAppId))
        .usePlaintext()
        .build();
    var service = RbacGrpc.newBlockingStub(channel);
    var channelDisposer = GrpcUtils.asCloseable(channel);
    return Closeable.of(service, channelDisposer);
  }
}
