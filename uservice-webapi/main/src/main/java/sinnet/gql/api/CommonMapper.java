package sinnet.gql.api;

import sinnet.gql.models.EntityGql;
import sinnet.gql.utils.PropsBuilder;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.projects.generated.ProjectId;

/** Mappings. */
public interface CommonMapper {

  /** Grpc -> Gql mapping. */
  public static EntityGql toGql(ProjectId it) {
    if (it == null) {
      return null;
    }
    var result = new EntityGql();
    result.setProjectId(it.getEId());
    result.setEntityId(it.getEId());
    result.setEntityVersion(it.getETag());
    return result;
  }

  /** Grpc -> Gql mapping. */
  public static EntityGql toGql(EntityId it) {
    if (it == null) {
      return null;
    }
    var result = new EntityGql();
    result.setProjectId(it.getProjectId());
    result.setEntityId(it.getEntityId());
    result.setEntityVersion(it.getEntityVersion());
    return result;
  }

  /** Gql -> Grpc mapping. */
  public static ProjectId toGrpc(EntityGql it) {
    if (it == null) {
      return null;
    }
    return PropsBuilder.build(ProjectId.newBuilder())
        .set(b -> b::setEId, it.getEntityId())
        .set(b -> b::setETag, it.getEntityVersion())
        .done().build();
  }

}
