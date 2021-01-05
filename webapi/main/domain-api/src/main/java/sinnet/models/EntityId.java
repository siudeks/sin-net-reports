package sinnet.models;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = EntityId.MyBuilder.class)
@Builder(builderClassName = "MyBuilder", toBuilder = true)
public final class EntityId {
    private UUID projectId;
    private UUID id;
    private int version;

    public static EntityId of(UUID projectId, UUID id, int version) {
        return new EntityId(projectId, id, version);
    }

    public static EntityId anyNew(UUID projectId) {
        return new EntityId(projectId, UUID.randomUUID(), 0);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class MyBuilder {
    }
}
