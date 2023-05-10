package net.onlex.api;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import net.onlex.api.AppApiMutation.ProjectEntity;
import net.onlex.api.AppApiMutation.ProjectId;

@RequiredArgsConstructor
public class SessionState {
    
    private final String userEmail;
    private final Collection<ProjectModel> createdProjects = new LinkedList<>();

    void on(OperatorAssigned event) {
    }

    void on(TimeentryCreated event) {
    }

    void on(ProjectCreated event) {
        var entity = event.entity();
        var projectAlias = event.projectAlias();
        createdProjects.add(new ProjectModel(entity, projectAlias));
    }

    void on(ProjectRemoved event) {
        var existing = createdProjects
            .stream()
            .filter(it -> it.entity.getEntity().entityId.equals(event.entity().getId())).findFirst();
        existing.ifPresent(createdProjects::remove);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ProjectModel getLastCreatedProject() {
        return createdProjects.stream().reduce(null, (v1, v2) -> v2);
    }

    public Optional<ProjectModel> getProjectByAlias(String projectAlias) {
        return createdProjects.stream()
            .filter(it -> it.alias.equals(projectAlias))
            .findFirst();
    }

    public static record ProjectModel(ProjectEntity entity, String alias) { }
}

sealed interface AppEvent { }

record ProjectCreated (ProjectEntity entity, String projectAlias) implements AppEvent { }
record ProjectRemoved (ProjectId entity) implements AppEvent { }
record TimeentryCreated () implements AppEvent { }
record OperatorAssigned () implements AppEvent { }

