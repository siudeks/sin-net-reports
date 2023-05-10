package sinnet.gql.api;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.mappers.ProjectsMapper;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.grpc.ProjectsGrpcService;
import sinnet.grpc.projects.generated.ListRequest;
import sinnet.grpc.projects.generated.UserStatsRequest;

@Controller
@RequiredArgsConstructor
@Slf4j
class ProjectsQueryList {

  private final ProjectsGrpcService service;

  @SchemaMapping
  List<ProjectEntityGql> list(ProjectsQuery self, @Argument String name) {
    var request = ListRequest.newBuilder()
        .setEmailOfRequestor(self.getRequestorEmail())
        .build();
    return service.list(request)
        .getProjectsList().stream().map(ProjectsMapper::toDto).toList();
  }

  public Integer numberOfProjects(ProjectsQuery self) {
    var request = UserStatsRequest.newBuilder()
        .setEmailOfRequestor(self.getRequestorEmail())
        .build();
    return service.userStats(request).getNumberOfProjects();
  }
}


