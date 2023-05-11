package sinnet.gql.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import sinnet.grpc.common.UserToken;
import sinnet.grpc.roles.GetRequest;
import sinnet.grpc.roles.RbacGrpc.RbacBlockingStub;
import sinnet.web.B2CauthenticationToken;

@Component
class AccessProviderJwt implements AccessProvider {

  private RbacBlockingStub service;

  @Override
  public WithResult with(String projectId) {
    service.get(null).getRole();
    
    var a = SecurityContextHolder.getContext().getAuthentication();
    var b = (B2CauthenticationToken) a;
    var primaryEmail = b.getPrincipal();
      
    var userToken = UserToken.newBuilder()
        .setProjectId(projectId)
        .setRequestorEmail(primaryEmail)
        .build();
    var request = GetRequest.newBuilder()
        .setUserToken(userToken)
        .build();
    var response = service.get(request);
    return WithResult.of(userToken, response.getRole());
  }
}
