package sinnet.domain.access;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.dbo.DboFacade;
import sinnet.grpc.projects.generated.UserToken;
import sinnet.domain.model.ValEmail;

@Component
@RequiredArgsConstructor
final class AccessAsOwner implements AccessAs<RoleContextSet.OwnerRoleContext> {

  private final DboFacade dbo;

  @Override
  public RoleContextSet.OwnerRoleContext calculate(UserToken userToken) {
    var ownerEmail = ValEmail.of(userToken.getRequestorEmail());
    var ownedProjects = dbo.ownedAsId(ownerEmail);
    return new RoleContextSet.OwnerRoleContext(ownedProjects);
  }
}
