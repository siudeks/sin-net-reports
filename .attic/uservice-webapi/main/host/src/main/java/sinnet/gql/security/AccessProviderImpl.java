package sinnet.gql.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import sinnet.gql.Transform;
import sinnet.grpc.GrpcRbac;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.roles.GetRequest;

@ApplicationScoped
@Slf4j
public class AccessProviderImpl implements AccessProvider {

    @Inject GrpcRbac projectsGrpc;
  
    @Inject
    JsonWebToken jwt;
  
    @Override
    public Uni<WithResult> with(String projectId) {
        var emailsAsObject = jwt.getClaim("emails");
        var emails = (JsonArray) emailsAsObject;
        var firstEmail = emails.getString(0);
        
        var userToken = UserToken.newBuilder()
            .setProjectId(projectId)
            .setRequestorEmail(firstEmail)
            .build();
        var request = GetRequest.newBuilder()
            .setUserToken(userToken)
            .build();
        return projectsGrpc
            .get(request)
            .onItemOrFailure()
            .transform(Transform.secured(log, it -> new WithResult(userToken, it.getRole())));    
    }
}
