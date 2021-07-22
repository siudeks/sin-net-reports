package sinnet;

import java.util.UUID;

import io.vertx.core.Future;
import sinnet.models.ActionValue;
import sinnet.models.Entity;
import sinnet.models.EntityId;

/**
 * Basic operations on store / get an entity from database.
 */
// TODO Move behing bus
// TODO add gherkin test
public interface ActionRepository {

  Future<Boolean> save(EntityId entityId, ActionValue entity);

  Future<EntityId> update(EntityId id, ActionValue entity);

  Future<Entity<ActionValue>> find(UUID projectId, UUID entityId);

  Future<Boolean> remove(EntityId id);
}
