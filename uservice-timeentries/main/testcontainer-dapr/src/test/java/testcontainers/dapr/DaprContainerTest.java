package testcontainers.dapr;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;

/** run DAPR sidecar used for testing. */
final class DaprContainerTest {

  @Container
  DaprContainer container = new DaprContainer();
  
  @Test
  void should() {
    var ports = container.getExposedPorts();
    var client = new DaprClientBuilder().build();
    client.publishEvent("a", "b", "c", Map.of());
  }
  
}
