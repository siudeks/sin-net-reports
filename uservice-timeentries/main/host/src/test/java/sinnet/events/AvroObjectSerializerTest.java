package sinnet.events;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.dapr.utils.TypeRef;
import lombok.SneakyThrows;
import sinnet.project.events.ProjectCreatedEvent;

class AvroObjectSerializerTest {
  
  @Test
  @SneakyThrows
  void should_serialize_and_deserialize_example_avro_type() {
    var example = ProjectCreatedEvent.newBuilder()
        .setEid("my id")
        .setEtag(1)
        .build();
    var ser = new AvroObjectSerializer();
    var asByte = ser.serialize(example);
    var s = new String(asByte);
    var actual = ser.deserialize(asByte, TypeRef.get(ProjectCreatedEvent.class));
    Assertions.assertThat(actual).isEqualTo(example);
  }
}
