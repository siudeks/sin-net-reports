package sinnet.web;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import sinnet.grpc.projects.RpcFacade;

@Component
@Endpoint(id = "grpc")
@RequiredArgsConstructor
class RpcActuatorEndpoint {

  private final RpcFacade rpcFacade;
  
  @ReadOperation
  public RpcActuatorGetModel getPort() {
    var port = rpcFacade.getServerPort().orElse(-1);
    return new RpcActuatorGetModel(port);
  }

  @Data
  @AllArgsConstructor
  static class RpcActuatorGetModel {
    final int port;
  }

}
