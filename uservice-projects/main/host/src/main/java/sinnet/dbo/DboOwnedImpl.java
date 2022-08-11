package sinnet.dbo;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vavr.Function1;
import io.vavr.collection.Array;
import lombok.RequiredArgsConstructor;
import sinnet.grpc.projects.Project;
import sinnet.grpc.projects.ProjectId;
import sinnet.grpc.projects.ProjectModel;
import sinnet.model.Email;
import sinnet.model.ProjectIdHolder;

@ApplicationScoped
@RequiredArgsConstructor
final class DboOwnedImpl implements DboOwned {

  private final ProjectRepository projectRepository;

  private ProjectIdHolder mapToIdHolder(ProjectDbo dbo) {
    return ProjectIdHolder.of(dbo.getEntityId());
  }

  private ProjectId mapToId(ProjectDbo dbo) {
    return ProjectId.newBuilder()
      .setEId(dbo.getEntityId().toString())
      .setETag(dbo.getVersion())
      .build();
  }

  private ProjectModel mapToModel(ProjectDbo dbo) {
    return ProjectModel.newBuilder()
      .setEmailOfOwner(dbo.getEmailOfOwner())
      .setName(dbo.getName())
      .build();
  }

  private Project mapToEntity(ProjectDbo dbo) {
    return Project.newBuilder()
      .setId(mapToId(dbo))
      .setModel(mapToModel(dbo))
      .build();
  }

  @Override
  public Uni<Array<Project>> ownedAsProject(Email ownerEmail) {
    return ownedAndMap(ownerEmail, this::mapToEntity);
  }

  @Override
  public Uni<Array<ProjectIdHolder>> ownedAsId(Email ownerEmail) {
    return ownedAndMap(ownerEmail, this::mapToIdHolder);
  }

  private <T> Uni<Array<T>> ownedAndMap(Email emailOfOwner, Function1<ProjectDbo, T> mapper) {
    return projectRepository.list("select t from ProjectDbo t where t.emailOfOwner = ?1", emailOfOwner.value())
      .map(it -> Array.ofAll(it).map(mapper));
  }

}
