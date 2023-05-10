package sinnet.dapr;

import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.PublishEventRequest;

import jakarta.annotation.PostConstruct;

@Component
@Profile("!test")
class DaprConnectionImpl implements DaprConnection, AutoCloseable {
  
  private DaprClient client;

  @PostConstruct
  void init() {
    client = new DaprClientBuilder()
        .build();
  }

  @EventListener
  public void onEvent(PublishEventRequest event) {
    client.publishEvent(event);
  }

  @Override
  public void close() throws Exception {
    client.close();
  }

}
