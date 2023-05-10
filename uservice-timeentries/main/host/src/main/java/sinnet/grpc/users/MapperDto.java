package sinnet.grpc.users;

import java.util.Optional;
import java.util.UUID;

import io.vavr.collection.List;
import sinnet.grpc.common.Mapper;
import sinnet.grpc.mapping.PropsBuilder;
import sinnet.read.UserModel;

/**
 * Domain <-> Dto translations.
 */
public interface MapperDto extends Mapper {

  /**
   * TBD.
   */
  default java.util.List<sinnet.grpc.users.UsersSearchModel> toDto(List<UserModel> model) {
    return model.map(this::toDto).toJavaList();
  }

  /**
   * TBD.
   */
  default sinnet.grpc.users.UsersSearchModel toDto(UserModel model) {
    return PropsBuilder.build(sinnet.grpc.users.UsersSearchModel.newBuilder())
        .tset(Optional.ofNullable(model.getEmail().getValue()), b -> b::setEmail)
        .set(UUID.randomUUID().toString(), b -> b::setEntityId)
        .done().build();
  }
}
