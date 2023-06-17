package sinnet.grpc;

import java.util.List;

import io.vavr.Function1;
import sinnet.domain.ProjectId;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.grpc.projects.generated.Project;

/** Mockable equivalent of {@link ProjectsGrpcStub}. */
public interface ProjectsGrpcFacade {

  ProjectId create(String requestorEmail);

  ProjectId update(String requestorEmail, ProjectId id, String name, String ownerEmail, List<String> operatorEmail);

  List<ProjectEntityGql> list(String requestorEmail, Function1<Project, ProjectEntityGql> mapper);

  StatsResult userStats(String requestorEmail);

  /**
   * Set of stats related to the current user. Scope of statis is defined by needs of getting info about the user.
   */
  record StatsResult(int numberOfProjects) {
  }
}
