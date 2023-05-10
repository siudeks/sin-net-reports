package bdd;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import sinnet.grpc.ProjectsGrpcService;

/**
 * Place where we keep all ports which are mocked as part of all DDD scenarions.
 */
class PortsConfigurer {

  @Bean()
  @Primary
  ProjectsGrpcService projectsGrpcServiceMocked() {
    var projectsGrpcService = Mockito.mock(ProjectsGrpcService.class);
    return projectsGrpcService;
  }

}
