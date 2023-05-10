package sinnet.grpc.projects;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import sinnet.grpc.projects.generated.CreateReply;
import sinnet.grpc.projects.generated.CreateRequest;
import sinnet.grpc.projects.generated.GetReply;
import sinnet.grpc.projects.generated.GetRequest;
import sinnet.grpc.projects.generated.ListReply;
import sinnet.grpc.projects.generated.ListRequest;
import sinnet.grpc.projects.generated.ProjectsGrpc.ProjectsImplBase;
import sinnet.grpc.projects.generated.RemoveCommand;
import sinnet.grpc.projects.generated.RemoveResult;
import sinnet.grpc.projects.generated.UpdateCommand;
import sinnet.grpc.projects.generated.UpdateResult;
import sinnet.grpc.projects.generated.UserStatsReply;
import sinnet.grpc.projects.generated.UserStatsRequest;

@Component
@RequiredArgsConstructor
class ProjectsService extends ProjectsImplBase {

  private final ProjectsCreateImpl projectsCreate;
  private final ProjectsUpdateImpl projectsUpdate;
  private final ProjectListImpl projectsList;
  private final ProjectGetImpl projectsGet;
  private final ProjectsRemoveImpl projectsRemove;
  private final ProjectUserStatsImpl projectUserStats;

  @Override
  public void create(CreateRequest request, StreamObserver<CreateReply> responseObserver) {
    projectsCreate.command(request, responseObserver);
  }

  @Override
  public void update(UpdateCommand request, StreamObserver<UpdateResult> responseObserver) {
    projectsUpdate.command(request, responseObserver);
  }

  @Override
  public void list(ListRequest request, StreamObserver<ListReply> responseObserver) {
    projectsList.query(request, responseObserver);
  }  

  @Override
  public void get(GetRequest request, StreamObserver<GetReply> responseObserver) {
    projectsGet.query(request, responseObserver);
  }

  @Override
  public void remove(RemoveCommand request, StreamObserver<RemoveResult> responseObserver) {
    projectsRemove.command(request, responseObserver);
  }

  @Override
  public void userStats(UserStatsRequest request, StreamObserver<UserStatsReply> responseObserver) {
    projectUserStats.query(request, responseObserver);
  }

}
