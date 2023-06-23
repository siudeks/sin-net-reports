package sinnet.grpc;

import java.util.List;

import org.springframework.stereotype.Component;

import io.vavr.Function1;
import lombok.RequiredArgsConstructor;
import sinnet.domain.ProjectId;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.grpc.projects.generated.CreateRequest;
import sinnet.grpc.projects.generated.ListRequest;
import sinnet.grpc.projects.generated.Project;
import sinnet.grpc.projects.generated.ProjectModel;
import sinnet.grpc.projects.generated.ProjectsGrpc.ProjectsBlockingStub;
import sinnet.grpc.projects.generated.UpdateCommand;
import sinnet.grpc.projects.generated.UserStatsRequest;
import sinnet.grpc.projects.generated.UserToken;


/** Mockable equivalent of {@link ProjectsGrpcStub}. */
@Component
@RequiredArgsConstructor
class ProjectsGrpcFacadeImpl implements ProjectsGrpcFacade {

  private final ProjectsBlockingStub stub;

  @Override
  public ProjectId create(String requestorEmail) {
    var requestorToken = UserToken.newBuilder()
        .setRequestorEmail(requestorEmail)
        .build();
    var createRequest = CreateRequest.newBuilder()
        .setUserToken(requestorToken)
        .build();

    var createResult = stub.create(createRequest);
    var id = createResult.getEntityId();

    return new ProjectId(id.getEId(), id.getETag());
  }

  @Override
  public ProjectId update(String requestorEmail, ProjectId id, String name, String ownerEmail, List<String> operatorEmail) {
    var requestorToken = UserToken.newBuilder()
        .setRequestorEmail(requestorEmail)
        .build();
    var desired = ProjectModel.newBuilder()
        .setName(name)
        .setEmailOfOwner(requestorEmail);
    var updateRequest = UpdateCommand.newBuilder()
        .setUserToken(requestorToken)
        .setEntityId(sinnet.grpc.projects.generated.ProjectId.newBuilder()
          .setEId(id.id())
          .setETag(id.tag()))
        .setDesired(desired)
        .build();
    var updatedId = stub.update(updateRequest).getEntityId();
    return new ProjectId(updatedId.getEId(), updatedId.getETag());
  }

  @Override
  public List<ProjectEntityGql> list(String requestorEmail, Function1<Project, ProjectEntityGql> mapper) {
    var request = ListRequest.newBuilder()
        .setEmailOfRequestor(requestorEmail)
        .build();
    return stub.list(request)
        .getProjectsList()
        .stream()
        .map(mapper::apply)
        .toList();
  }

  @Override
  public StatsResult userStats(String requestorEmail) {
    var request = UserStatsRequest.newBuilder()
        .setEmailOfRequestor(requestorEmail)
        .build();
    var dto = stub.userStats(request);
    return new StatsResult(dto.getNumberOfProjects());
  }

}
