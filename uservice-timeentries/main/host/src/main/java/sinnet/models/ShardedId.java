package sinnet.models;

import java.util.UUID;

import lombok.Value;

/**
 * TBD.
 */
@Value
public class ShardedId {

  private UUID projectId;
  private UUID id;
  private long version;

  public ShardedId next() {
    return new ShardedId(this.projectId, this.id, version + 1);
  }

  public static ShardedId of(UUID projectId, UUID id, long version) {
    return new ShardedId(projectId, id, version);
  }

  public static ShardedId of(Entity<?> entity) {
    return new ShardedId(entity.getProjectId(), entity.getEntityId(), entity.getVersion());
  }

  public static ShardedId anyNew(UUID projectId) {
    return new ShardedId(projectId, UUID.randomUUID(), 0);
  }

  public static ShardedId anyNew(ProjectId projectId) {
    return anyNew(projectId.getId());
  }
}
