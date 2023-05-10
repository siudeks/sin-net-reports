package sinnet.dbo;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TBD.
 */
@Entity
@Table(name = "actions")
@Data
@Accessors(chain = true)
class ActionDbo {

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
