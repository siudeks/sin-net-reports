package sinnet.grpc.roles;

import sinnet.grpc.roles.GetReply.Role;
import sinnet.read.RolesProjector;

interface Mapper {

  default Role toDto(RolesProjector.Role role) {
    
    if (role == RolesProjector.Role.USER) {
      return Role.SERVICEMAN;
    }
    return Role.NONE;
  }
}

