package sinnet.grpc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import sinnet.domain.EntityId;
import sinnet.gql.api.CommonMapper;
import sinnet.gql.models.ServiceModelGql;
import sinnet.grpc.timeentries.TimeEntryModel;

/** GRPC methods related to Actions (aka Services aka Timeentries). */
public interface ActionsGrpcFacade {

  /** REturns list of actions for requested project, limited result from-to range. */
  List<TimeEntryModel> searchInternal(UUID projectId, LocalDate from, LocalDate to);

  /** Fixme. */
  EntityId newAction(String requestorEmail, UUID projectId, LocalDate when);

  /** Fixme. */
  boolean update(EntityId entityId, String customerId, String description, int distance, int duration,
                 String servicemanEmail, String servicemanName, LocalDate whenProvided);
  
  /** Fixme. */
  TimeEntryModel getActionInternal(UUID projectId, UUID entityId);

  default ServiceModelGql getAction(UUID projectId, UUID entityId) {
    var dto =  getActionInternal(projectId, entityId);
    return map(dto);
  }
  
  /** Returns list of actions for requested project, limited result from-to range. */
  default List<ServiceModelGql> search(UUID projectId, LocalDate from, LocalDate to) {
    return searchInternal(projectId, from, to).stream().map(ActionsGrpcFacade::map).toList();
  }

  /** Internal mapping. */
  static ServiceModelGql map(TimeEntryModel model) {
    return new ServiceModelGql()
        .setDescription(model.getDescription())
        .setDistance(model.getDistance())
        .setDuration(model.getDuration())
        .setEntityId(model.getEntityId().getEntityId())
        .setEntityVersion(model.getEntityId().getEntityVersion())
        .setProjectId(model.getEntityId().getProjectId())
        .setServicemanEmail(model.getServicemanEmail())
        .setServicemanName(model.getServicemanName())
        .setWhenProvided(CommonMapper.toGql(model.getWhenProvided()));
  }

}
