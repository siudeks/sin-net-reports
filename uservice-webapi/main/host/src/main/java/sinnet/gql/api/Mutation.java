package sinnet.gql.api;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
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
}
