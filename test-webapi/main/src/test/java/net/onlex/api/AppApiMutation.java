package net.onlex.api;

import java.time.LocalDate;

import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;

import io.smallrye.graphql.client.typesafe.api.NestedParameter;
import lombok.Data;
import lombok.Value;

public interface AppApiMutation {

  @Mutation("Projects")
  SaveProjectResult saveProject(@NestedParameter("save") @NonNull String name);

  @Mutation("Projects")
  RemoveProjectQuery removeProject(@NestedParameter("remove") @NonNull ProjectId projectId);

  @Mutation("Actions")
  NewActionResult newAction(@NonNull @Id String projectId, @NestedParameter("newAction") @NonNull LocalDate whenProvided);

  @Mutation("Projects")
  WithOperatorResult assignOperator(@NestedParameter("project") @NonNull @Id String eid,
      @NestedParameter("project") @NonNull @Id long etag,
      @NestedParameter("project.withOperator") @NonNull String operatorEmail);

  @Data
  class WithOperatorResult {
    WithOperatorResult2 project;
  }
  @Data
  class WithOperatorResult2 {
    ProjectIdModel withOperator;
  }

  @Data
  class NewActionResult {
    NewActionResultNewAction newAction;
  }
  @Data
  class NewActionResultNewAction {
    String entityId;
    Long entityVersion;
    String projectId;
  }


  @Data
  class ProjectInfo {
    private String id;
    private String name;
  }


  @Data
  class SaveProjectResult {
    ProjectEntity save;
  }

  @Data
  class RemoveProjectQuery {
    Boolean remove;
  }

  @Data
  class EntityInput {
    String projectId;
    String entityId;
    int entityVersion;
  }

  @Data
  class ProjectEntity {
    private @NonNull EntityInput entity;
    private @NonNull String name;
  }

  @Value
  class ProjectId {
    private String id;
    private long tag;
  }

  @Data
  class ProjectIdModel {
    private String id;
    private long tag;
  }

}
