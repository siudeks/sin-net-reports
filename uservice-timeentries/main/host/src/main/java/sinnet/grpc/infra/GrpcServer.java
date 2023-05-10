package sinnet.grpc.infra;

import java.util.OptionalInt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.SneakyThrows;
import sinnet.grpc.projects.RpcFacade;

/** Registers all discoverable gRpc services to allow them be reachable. */
@Component
public class GrpcServer implements RpcFacade, AutoCloseable {

  private final BindableService[] services;
  private final int port;
  private OptionalInt serverPort = OptionalInt.empty();

  public GrpcServer(BindableService[] services,
                    @Value("${grpc.server-port}") int port) {
    this.services = services;
    this.port = port;
  }

  private Server server;

  /**
   * TBD.
   */
  @jakarta.annotation.PostConstruct
  @SneakyThrows
  public void start() {
    var builder = ServerBuilder.forPort(port);
    for (var bindableService : services) {
      builder.addService(bindableService);
    }
    server = builder
      .intercept(new ExceptionHandler())
      .build();
    server.start();
    serverPort = OptionalInt.of(server.getPort());
  }

  @Override
  public void close() throws Exception {
    server.shutdownNow();
  }

  @Override
  public OptionalInt getServerPort() {
    return serverPort;
  }

}
