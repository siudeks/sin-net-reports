package sinnet.gql;

import java.time.LocalDate;

import sinnet.gql.models.Mapper;
import sinnet.gql.models.ServiceEntry;
import sinnet.gql.utils.PropsBuilder;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.timeentries.GetReply;
import sinnet.grpc.timeentries.TimeEntryModel;

public interface TimeentriesMapper extends Mapper {

  /**
   * DTO -> GQL translation.
   * @param dto
   * @param userToken
   * @return
   */
  default ServiceModel toGql(GetReply dto, UserToken userToken) {
    var item = dto.getItem();
    if (item == null) {
      return null;
    }
    var it = new ServiceModel(); 
    it.setProjectId(item.getEntityId().getProjectId());
    it.setEntityId(item.getEntityId().getEntityId());
    it.setEntityVersion(item.getEntityId().getEntityVersion());
    it.setServicemanEmail(item.getServicemanEmail());
    it.setServicemanName(item.getServicemanName());
    it.setWhenProvided(toGql(item.getWhenProvided()));
    it.setDescription(item.getDescription());
    it.setDistance(item.getDistance());
    it.setDuration(item.getDuration());

    it.setCustomerId(item.getCustomerId());
    it.setUserToken(userToken);
    return it;
  }

    default TimeEntryModel toGrpc(EntityId eid, ServiceEntry it) {
        if (it == null) return null;
        return PropsBuilder.build(TimeEntryModel.newBuilder())
            .set(b -> b::setEntityId, eid)
            .set(b -> b::setCustomerId, it.getCustomerId())
            .set(b -> b::setServicemanName, it.getServicemanName())
            .set(b -> b::setWhenProvided, toGrpc(it.getWhenProvided()))
            .set(b -> b::setDescription, it.getDescription())
            .set(b -> b::setDistance, it.getDistance())
            .set(b -> b::setDuration, it.getDuration())
            .done().build();
    }

    default sinnet.grpc.timeentries.LocalDate toGrpc(LocalDate it) {
        if (it == null) return null;
        return sinnet.grpc.timeentries.LocalDate.newBuilder()
            .setYear(it.getYear())
            .setMonth(it.getMonthValue())
            .setDay(it.getDayOfMonth())
            .build();
    }

    default LocalDate fromGrpc(sinnet.grpc.timeentries.LocalDate it) {
        if (it == null) return null;
        return LocalDate.of(it.getYear(), it.getMonth(), it.getDay());
    }
}
