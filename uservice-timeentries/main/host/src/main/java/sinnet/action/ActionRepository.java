package sinnet.action;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lombok.Data;
import lombok.experimental.Accessors;

@Repository
public interface ActionRepository extends JpaRepository<ActionRepository.ActionDbo, UUID> {
  
  void deleteByProjectidEntityidEntityversion(UUID projectId, UUID entityId, long version);
  ActionDbo findByProjectidEntityidEntityversion(UUID projectId, UUID entityId, long version);

  @Entity
  @Table(name = "actions")
  @Data
  @Accessors(chain = true)
  final class ActionDbo {

    @Column(name = "project_id")
    private UUID projectId;

    @Id
    @Column(name = "entity_id")
    private UUID entityId;

    @Version
    @Column(name = "entity_version")
    private Long entityVersion;

    @Column(name = "serviceman_email")
    private String servicemanEmail;

    @Column(name = "description")
    private String description;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "serviceman_name")
    private String servicemanName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "customer_id")
    private UUID customerId;
  }
}
