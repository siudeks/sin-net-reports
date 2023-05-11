package sinnet.gql.api;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import sinnet.gql.mappers.ProjectsMapper;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.grpc.ProjectsGrpcFacade;

@Controller
@RequiredArgsConstructor
class ProjectsQueryList {

  private final ProjectsGrpcFacade service;

  @SchemaMapping
  List<ProjectEntityGql> list(ProjectsQuery self, @Argument String name) {
    var requestorEmail = self.getRequestorEmail();
    var result = service.list(requestorEmail, ProjectsMapper::toDto);
    return result;
  }

  public Integer numberOfProjects(ProjectsQuery self) {
    var requestorEmail = self.getRequestorEmail();
    return service.userStats(requestorEmail).numberOfProjects();
  }
}


