package sinnet.dbo;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "projects")
@Data
@Accessors(chain = true)
class ProjectDbo {

  @Id
  @Column(name = "ENTITY_ID")
  private UUID entityId;

  @Version
  @Column(name = "ENTITY_VERSION")
  private Long version;

  @Column(name = "EMAIL_OF_OWNER")
  @Size(max = 50, message = "ERR:0001")
  private String emailOfOwner;

  @Column(name = "NAME")
  @Size(max = 50, message = "ERR:0002")
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "PROJECT_OPERATOR", joinColumns = @JoinColumn(name = "project_id"))
  @Column(name = "email")
  private Set<String> operators;
}


