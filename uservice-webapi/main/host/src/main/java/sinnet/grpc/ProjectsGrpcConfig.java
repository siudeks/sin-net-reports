package sinnet.grpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import sinnet.grpc.GrpcProperties.ServiceAddress;
import sinnet.grpc.projects.generated.ProjectsGrpc;
import sinnet.grpc.projects.generated.ProjectsGrpc.ProjectsBlockingStub;

@Configuration
@Slf4j
class ProjectsGrpcConfig {

  @Bean
  ProjectsBlockingStub projectsBlockingStub(Closeable.Of<ProjectsBlockingStub> closeableProjectsBlockingStub) {
    return closeableProjectsBlockingStub.item();
  }

  @Bean
  Closeable.Of<ProjectsBlockingStub> closeableProjectsBlockingStub(GrpcProperties props) {
    return managedChannel(props.getProjects());
  }

  Closeable.Of<ProjectsBlockingStub> managedChannel(ServiceAddress address) {
    log.info("Configure gRpc connection to Projects service: {}", address);
    var daprAppId = address.getDaprAppId();
    var channel = ManagedChannelBuilder.forAddress(address.getHost(), address.getPort())
        .intercept(GrpcUtils.addTargetDaprApplicationId(daprAppId))
        .usePlaintext()
        .build();
    var service = ProjectsGrpc.newBlockingStub(channel);
    var channelDisposer = GrpcUtils.asCloseable(channel);
    return Closeable.of(service, channelDisposer);
  }
}
