package net.onlex.api;

import java.util.List;

import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

import io.smallrye.graphql.client.typesafe.api.NestedParameter;
import lombok.Data;

public interface AppApiQuery {

  @Query("Projects")
  ProjectListQuery projectList(@NestedParameter("list") @NonNull String name);

  @Query("Projects")
  ProjectsQuery3 projectsCount();

  @Data
  class ProjectsQuery3 {
    private int numberOfProjects;
  }

  @Data
  class ProjectListQuery {
    private List<ProjectEntity> list;
  }

  @Data
  class Entity {
    private String projectId;
    private String entityId;
    private int entityVersion;
  }

  @Data
  class ProjectEntity {
    private @NonNull Entity entity;
    private @NonNull String name;
  }

}
