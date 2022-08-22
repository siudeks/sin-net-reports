package sinnet.dbo;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

@ApplicationScoped
class ProjectRepository implements PanacheRepositoryBase<ProjectDbo, Long> {
}
