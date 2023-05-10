package sinnet.grpc;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import sinnet.grpc.projects.generated.ListReply;
import sinnet.grpc.projects.generated.UserStatsReply;
import sinnet.grpc.projects.generated.UserStatsRequest;
import sinnet.grpc.projects.generated.ProjectsGrpc.ProjectsBlockingStub;

/** Mockable equivalent of {@link ProjectsGrpcStub}. */
@Component
@RequiredArgsConstructor
public class ProjectsGrpcService {

  private interface ProjectsService {
    public ListReply list(sinnet.grpc.projects.generated.ListRequest request);

    public UserStatsReply userStats(UserStatsRequest request);
  }

  @Delegate(types = ProjectsService.class)
  private final ProjectsBlockingStub stub;

}
