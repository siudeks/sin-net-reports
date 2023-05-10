package onlexnet.sinnet.actests.api;

import java.util.List;

import lombok.Data;

public interface AppApiQuery {

  // @Query("Projects")
  // ProjectListQuery projectList(@NestedParameter("list") @NonNull String name);

  // @Query("Projects")
  // ProjectsQuery3 projectsCount();

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
    private Entity entity;
    private String name;
  }

}
