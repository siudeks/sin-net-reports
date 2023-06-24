package sinnet.bdd;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import sinnet.grpc.CustomersGrpcService;
import sinnet.grpc.ProjectsGrpcFacade;
import sinnet.grpc.UsersGrpcService;

/**
 * Place where we keep all ports which are mocked as part of all DDD scenarions.
 * AS I am in r
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

  @Bean
  @Primary
  UsersGrpcService usersGrpcServiceMocked() {
    var service = Mockito.mock(UsersGrpcService.class);
    return service;
  }

}
