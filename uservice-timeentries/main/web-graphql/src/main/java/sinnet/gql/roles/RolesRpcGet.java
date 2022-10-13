package sinnet.gql.roles;

import java.util.UUID;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sinnet.grpc.roles.GetReply;
import sinnet.grpc.roles.GetRequest;
import sinnet.models.Email;
import sinnet.read.RolesProjector;

@Component
@Slf4j
@AllArgsConstructor
public class RolesRpcGet implements Mapper {
    private final RolesProjector.Provider rolesProjector;

    public void query(GetRequest request, StreamObserver<GetReply> responseObserver) {
      var userToken = request.getUserToken();
      var emailAsString = userToken.getRequestorEmail();
      var email = Email.of(emailAsString);
      var projectIdAsString = userToken.getProjectId();
      var projectId = UUID.fromString(projectIdAsString);
      var result = rolesProjector.find(email, projectId);
      var reply = GetReply.newBuilder()
          .setRole(toDto(result))
          .build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
}
