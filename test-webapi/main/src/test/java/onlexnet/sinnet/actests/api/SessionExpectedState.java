package onlexnet.sinnet.actests.api;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import onlexnet.sinnet.actests.api.AppApiMutation.ProjectId;
import sinnet.gql.models.ProjectEntityGql;

@RequiredArgsConstructor
public class SessionExpectedState {
    
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
            .filter(it -> it.entity.getEntity().getEntityId().equals(event.entity().getId())).findFirst();
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

    public static record ProjectModel(ProjectEntityGql entity, String alias) { }
}

sealed interface AppEvent { }

record ProjectCreated (ProjectEntityGql entity, String projectAlias) implements AppEvent { }
record ProjectRemoved (ProjectId entity) implements AppEvent { }
record TimeentryCreated () implements AppEvent { }
record OperatorAssigned () implements AppEvent { }

