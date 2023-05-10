package sinnet.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.domain.project.ProjectRepository;

@Component
@RequiredArgsConstructor
class ProjectCreatedEventHandler {

  private final ProjectRepository repository;

  @EventListener
  void on(ProjectCreated event) {
    var newEntity = new ProjectRepository.DboTemplate()
        .setEntityId(event.getProjectId())
        .setEntityVersion(event.getProjectVersion())
        .setName(event.getName());
    repository.save(newEntity);
  }

}
