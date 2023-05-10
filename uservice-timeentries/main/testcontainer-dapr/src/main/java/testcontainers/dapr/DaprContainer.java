package testcontainers.dapr;

import org.testcontainers.containers.GenericContainer;

/** run DAPR sidecar used for testing. */
public final class DaprContainer extends GenericContainer<DaprContainer>  {

  public DaprContainer() {
    // we use mariner-base image
    // more: https://docs.dapr.io/operations/hosting/kubernetes/kubernetes-deploy/#using-mariner-based-images
    super("daprio/dapr:1.10.5-mariner");
  }

  @Override
  protected void configure() {
    super.setCommand("./daprd");

    // https://docs.dapr.io/reference/cli/dapr-run/
    // super.comm
    //   super.configure();
    //   .WithImage(DaprImage)
    //   .WithEntrypoint("./daprd")
    //   .WithCommand("-dapr-http-port", DaprHttpPort.ToString())
    //   .WithCommand("-dapr-grpc-port", DaprGrpcPort.ToString())
    //   .WithPortBinding(DaprHttpPort, true)
    //   .WithPortBinding(DaprGrpcPort, true)
    //   .WithWaitStrategy(Wait.ForUnixContainer().UntilHttpRequestIsSucceeded(request =>
    //       request.ForPath("/v1.0/healthz").ForPort(DaprHttpPort).ForStatusCode(HttpStatusCode.NoContent)));
  }

}
