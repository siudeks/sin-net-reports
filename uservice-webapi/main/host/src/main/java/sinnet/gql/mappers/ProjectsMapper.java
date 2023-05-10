package sinnet.gql.mappers;

import java.util.UUID;

import sinnet.gql.api.CommonMapper;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.gql.models.ProjectIdGql;
import sinnet.gql.models.SomeEntityGql;
import sinnet.grpc.projects.generated.Project;
import sinnet.grpc.projects.generated.ProjectId;

/** Fixme. */
public interface ProjectsMapper extends CommonMapper {

  /** Fixme. */
  public static ProjectEntityGql toDto(Project grpc) {
    return new ProjectEntityGql()
      .setEntity(new SomeEntityGql()
          .setEntityId(grpc.getId().getEId())
          .setProjectId(grpc.getId().getEId())
          .setEntityVersion(grpc.getId().getETag()))
      .setName(grpc.getModel().getName());
  }

  /** Fixme. */
  public static ProjectId toGrpc(ProjectIdGql grpc) {
    var entityId = UUID.fromString(grpc.getId());
    var entityTag = grpc.getTag();
    return sinnet.grpc.projects.generated.ProjectId.newBuilder()
        .setEId(entityId.toString())
        .setETag(entityTag)
        .build();
  }

  /** Fixme. */
  public static ProjectIdGql toGql(ProjectId grpc) {
    return new ProjectIdGql()
        .setId(grpc.getEId())
        .setTag(grpc.getETag());
  }
}
