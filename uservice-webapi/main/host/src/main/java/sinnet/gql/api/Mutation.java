package sinnet.gql.api;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import sinnet.grpc.common.UserToken;
import sinnet.web.B2CauthenticationToken;

@Controller
@RequiredArgsConstructor
class Mutation {
  
  @MutationMapping("Projects")
  ProjectsMutation projects() {
    var authentication = (B2CauthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    var primaryEmail = authentication.getPrincipal();

    return new ProjectsMutation(primaryEmail);
  }

  @MutationMapping("Customers")
  CustomersMutation customers(@Argument String projectId) {
    var authentication = (B2CauthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    var primaryEmail = authentication.getPrincipal();

    var userToken = UserToken.newBuilder()
        .setProjectId(projectId)
        .setRequestorEmail(primaryEmail)
        .build();

    return new CustomersMutation(projectId, userToken);
  }
}
