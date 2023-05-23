package sinnet.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.dapr.v1.AppCallbackGrpc;
import io.dapr.v1.AppCallbackGrpc.AppCallbackBlockingStub;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import sinnet.AppOperations;
import sinnet.grpc.projects.RpcFacade;
import sinnet.grpc.projects.generated.ProjectsGrpc;
import sinnet.grpc.projects.generated.ProjectsGrpc.ProjectsBlockingStub;
import sinnet.grpc.timeentries.TimeEntriesGrpc;
import sinnet.grpc.timeentries.TimeEntriesGrpc.TimeEntriesBlockingStub;
import sinnet.grpc.users.UsersGrpc;
import sinnet.grpc.users.UsersGrpc.UsersBlockingStub;

@Component
public class RpcApi implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  RpcFacade rpcFacade;
  
  @Getter
  private UsersBlockingStub users;

  @Getter
  private TimeEntriesBlockingStub timeentries;

  @Getter
  private ProjectsBlockingStub projects;

  @Getter
  private AppCallbackBlockingStub apiCallback;

  AppOperations appOperations;
  
  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    var grpcPort = rpcFacade.getServerPort().getAsInt();

    var channel = ManagedChannelBuilder.forAddress("localhost", grpcPort)
        .usePlaintext()
        .build();
    
    users = UsersGrpc.newBlockingStub(channel);
    timeentries = TimeEntriesGrpc.newBlockingStub(channel);
    apiCallback = AppCallbackGrpc.newBlockingStub(channel);
    projects = ProjectsGrpc.newBlockingStub(channel);
  }
}
