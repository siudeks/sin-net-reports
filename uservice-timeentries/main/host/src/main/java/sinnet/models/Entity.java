package sinnet.models;

import java.util.UUID;

import lombok.Value;

/**
 * TBD.
 */
@Value
public class Entity<V> {

  @Deprecated
  private UUID projectId;
  @Deprecated
  private UUID entityId;
  @Deprecated
  private long version;

  public ShardedId getId() {
    return ShardedId.of(projectId, entityId, version);
  }

  private V value;
}
