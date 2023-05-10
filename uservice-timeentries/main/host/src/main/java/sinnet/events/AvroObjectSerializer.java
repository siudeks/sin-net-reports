package sinnet.events;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dapr.serializer.DaprObjectSerializer;
import io.dapr.utils.TypeRef;
import lombok.SneakyThrows;

/**
 * Not optimized ser/deser component for Avro generated classes.
 * Used: https://stackoverflow.com/questions/60703971/jsonmappingexception-not-a-map-not-an-array-or-not-an-enum
 */
public final class AvroObjectSerializer implements DaprObjectSerializer {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    OBJECT_MAPPER.addMixIn(
        org.apache.avro.specific.SpecificRecord.class, // Interface implemented by all generated Avro-Classes
        JacksonIgnoreAvroPropertiesMixIn.class);
  }

  @Override
  @SneakyThrows
  public byte[] serialize(Object o) throws IOException {
    var result = OBJECT_MAPPER.writeValueAsBytes(o);
    return result;
  }

  @Override
  @SneakyThrows
  public <T> T deserialize(byte[] data, TypeRef<T> type) throws IOException {
    var clazz = (Class<T>) type.getType();
    var result = OBJECT_MAPPER.readValue(data, clazz);
    return result;
  }

  @Override
  public String getContentType() {
    return "application/json";
  }

  /**
   * TBD.
   */
  public abstract class JacksonIgnoreAvroPropertiesMixIn {

    @JsonIgnore
    public abstract org.apache.avro.Schema getSchema();
  
    @JsonIgnore
    public abstract org.apache.avro.specific.SpecificData getSpecificData();
  }
}
