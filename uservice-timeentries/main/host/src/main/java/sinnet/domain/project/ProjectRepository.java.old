package sinnet.domain.project;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TBD.
 */
@Repository
public interface ProjectRepository extends JpaRepository<ProjectRepository.DboTemplate, UUID> {

  /**
   * TBD.
   */
  @Entity
  @Table(name = "projects")
  @Data
  @Accessors(chain = true)
  static class DboTemplate {

    @Id
    @Column(name = "entity_id")
    private UUID entityId;

    @Version
    @Column(name = "entity_version")
    private Long entityVersion;

    @Column(name = "name")
    private String name;

  }
}
