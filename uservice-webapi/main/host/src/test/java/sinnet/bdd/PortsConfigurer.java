package sinnet.bdd;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import sinnet.grpc.CustomersGrpcService;
import sinnet.grpc.ProjectsGrpcFacade;

/**
 * Place where we keep all ports which are mocked as part of all DDD scenarions.
 */
class PortsConfigurer {

  @Bean
  @Primary
  ProjectsGrpcFacade projectsGrpcServiceMocked() {
    var projectsGrpcService = Mockito.mock(ProjectsGrpcFacade.class);
    return projectsGrpcService;
  }

  @Bean
  @Primary
  CustomersGrpcService customersGrpcServiceMocked() {
    var service = Mockito.mock(CustomersGrpcService.class);
    return service;
  }

}
