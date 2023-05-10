package sinnet.dapr;

import java.io.ByteArrayOutputStream;

import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.dapr.client.domain.PublishEventRequest;
import lombok.SneakyThrows;
import sinnet.project.events.ProjectCreatedEvent;

@Component
class DaprEventConverter {
  
  @SneakyThrows
  @EventListener
  public PublishEventRequest on(ProjectCreatedEvent event) {
    var wr = new SpecificDatumWriter<>(ProjectCreatedEvent.class);
    var stream = new ByteArrayOutputStream();
    var encoder = EncoderFactory.get().jsonEncoder(ProjectCreatedEvent.SCHEMA$, stream);
    wr.write(event, encoder);
    encoder.flush();
    var data = stream.toByteArray();

    return new PublishEventRequest("pubsub", "MYTOPICNAME", data);
  }

}
