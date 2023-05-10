package sinnet.grpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.ManagedChannelBuilder;
import sinnet.grpc.GrpcProperties.ServiceAddress;
import sinnet.grpc.customers.CustomersGrpc;
import sinnet.grpc.customers.CustomersGrpc.CustomersBlockingStub;

@Configuration
class CustomersGrpcConfig {

  @Bean
  CustomersBlockingStub customersBlockingStub(Closeable.Of<CustomersBlockingStub> closeableCustomersBlockingStub) {
    return closeableCustomersBlockingStub.item();
  }

  @Bean
  Closeable.Of<CustomersBlockingStub> closeableCustomersBlockingStub(GrpcProperties props) {
    return managedChannel(props.getCustomers());
  }

  Closeable.Of<CustomersBlockingStub> managedChannel(ServiceAddress address) {
    var daprAppId = address.getDaprAppId();
    var channel = ManagedChannelBuilder.forAddress(address.getHost(), address.getPort())
        .intercept(GrpcUtils.addTargetDaprApplicationId(daprAppId))
        .usePlaintext()
        .build();
    var service = CustomersGrpc.newBlockingStub(channel);
    var channelDisposer = GrpcUtils.asCloseable(channel);
    return Closeable.of(service, channelDisposer);
  }
}
