package sinnet.grpc.actions;

import java.time.LocalDate;
import java.util.UUID;

import sinnet.grpc.mapping.PropsBuilder;
import sinnet.grpc.timeentries.TimeEntryModel;
import sinnet.models.ActionDuration;
import sinnet.models.ActionValue;
import sinnet.models.Distance;
import sinnet.models.ValEmail;
import sinnet.models.Entity;
import sinnet.models.ShardedId;

/**
 * TBD.
 */
public interface MapperDto extends sinnet.grpc.common.Mapper {

  /**
   * TBD.
   */
  default LocalDate fromDto(sinnet.grpc.timeentries.LocalDate it) {

    if (it == null) {
      return null;
    }
    return LocalDate.of(it.getYear(), it.getMonth(), it.getDay());
  }

  /**
   * TBD.
   */
  default Entity<ActionValue> fromDto(TimeEntryModel dto) {
    var projectIdAsString = dto.getEntityId().getProjectId();
    var projectId = UUID.fromString(projectIdAsString);
    var entityIdAsString = dto.getEntityId().getEntityId();
    var entityId = UUID.fromString(entityIdAsString);
    var entityVersion = dto.getEntityId().getEntityVersion();
    var customerIdAsString = dto.getCustomerId();
    var customerId = UUID.fromString(customerIdAsString);
    var value = new ActionValue()
        .setWho(ValEmail.of(dto.getServicemanEmail()))
        .setWhen(fromDto(dto.getWhenProvided()))
        .setWhom(customerId)
        .setWhat(dto.getDescription())
        .setHowLong(ActionDuration.of(dto.getDuration()))
        .setHowFar(Distance.of(dto.getDistance()));
    return value.withId(projectId, entityId, entityVersion);
  }

  /**
   * TBD.
  */
  default sinnet.grpc.timeentries.LocalDate.Builder toDto(LocalDate it) {

    if (it == null) {
      return null;
    }

    return sinnet.grpc.timeentries.LocalDate.newBuilder()
        .setYear(it.getYear())
        .setMonth(it.getMonthValue())
        .setDay(it.getDayOfMonth());
  }

  /**
   * TBD.
   */
  default TimeEntryModel toDto(Entity<ActionValue> it) {
    return PropsBuilder.build(TimeEntryModel.newBuilder())
      .set(it, o -> ShardedId.of(o), this::toDto, b -> b::setEntityId)
      .set(it.getValue().getWhom(), UUID::toString, b -> b::setCustomerId)
      .set(it.getValue().getWho().getValue(), b -> b::setServicemanName)
      .set(it.getValue().getWhen(), this::toDto, b -> b::setWhenProvided)
      .set(it.getValue().getWhat(), b -> b::setDescription)
      .set(it.getValue().getHowLong().getValue(), b -> b::setDuration)
      .set(it.getValue().getHowFar().getValue(), b -> b::setDistance)
      .done().build();
  }

}
