package sinnet.action;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sinnet.action.ActionRepository.ActionDbo;
import sinnet.models.ActionValue;
import sinnet.models.Entity;
import sinnet.models.ShardedId;
import sinnet.write.ActionRepositoryEx;

@Service
@RequiredArgsConstructor
public class ActionRepositoryExImpl implements ActionRepositoryEx, MapperDbo {

  private final ActionRepository repository;

  @Override
  public Boolean save(ShardedId entityId, ActionValue entity) {
    var model = entity.withId(entityId);
    var dbModel = toDbo(model);
    repository.save(dbModel);
    return true;
  }


  @Override
  public ShardedId update(Entity<ActionValue> entity) {
    var desired = entity;
    var template = toDbo(desired);
    var id = entity.getId();
    var actual = repository.findByProjectIdAndEntityIdAndEntityVersion(id.getProjectId(), id.getId(), id.getVersion());
    actual.setServicemanEmail(template.getServicemanEmail());
    actual.setDescription(template.getDescription());
    actual.setDistance(template.getDistance());
    actual.setDuration(template.getDuration());
    actual.setServicemanName(template.getServicemanName());
    actual.setDate(template.getDate());
    actual.setCustomerId(template.getCustomerId());
    return id.next();
  }

  @Override
  public Boolean remove(ShardedId id) {
    var projectId = id.getProjectId();
    var entityId = id.getId();
    var version = id.getVersion();
    repository.deleteByProjectIdAndEntityIdAndEntityVersion(projectId, entityId, version);
    return true;
  }


  @Override
  public List<Entity<ActionValue>> list(UUID projectId, LocalDate from, LocalDate to) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Entity<ActionValue> get(UUID projectId, UUID eid) {
    var dbo = repository.findByProjectIdAndEntityId(projectId, eid);
    return fromDbo(dbo);
  }

}

