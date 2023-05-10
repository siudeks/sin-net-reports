package sinnet.write;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import sinnet.models.ActionValue;
import sinnet.models.Entity;
import sinnet.models.ShardedId;

/**
 * Basic operations on store / get an entity from database.
 */
public interface ActionRepositoryEx {

  Boolean save(ShardedId entityId, ActionValue entity);

  ShardedId update(Entity<ActionValue> entity);

  Boolean remove(ShardedId id);

  List<Entity<ActionValue>> list(UUID projectId, LocalDate from, LocalDate to);

  Entity<ActionValue> get(UUID projectId, UUID eid);
}
