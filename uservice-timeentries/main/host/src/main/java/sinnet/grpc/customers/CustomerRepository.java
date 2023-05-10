package sinnet.grpc.customers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TBD.
 */
@Repository
interface CustomerRepository extends JpaRepository<CustomerDbo, UUID> {

  void deleteByProjectIdAndEntityIdAndEntityVersion(UUID projectId, UUID entityId, Long version);

  Stream<CustomerDbo> findByProjectId(UUID projectId);
  
  CustomerDbo findByProjectIdAndEntityId(UUID projectId, UUID entityId);

  /**
   * TBD.
   */
  @Data
  @Accessors(chain = true)
  @Embeddable
  class CustomerDboContact {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "email")
    private String email;
  }

  /**
   * TBD.
   */
  @Data
  @Accessors(chain = true)
  @Embeddable
  class CustomerDboSecret {

    @Column(name = "location")
    private String location;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    
    @Column(name = "changed_who")
    private String changedWho;

    @Column(name = "changed_when")
    private LocalDateTime changedWhen;
  }

  /**
   * TBD.
   */
  @Data
  @Accessors(chain = true)
  @Embeddable
  class CustomerDboSecretEx {

    @Column(name = "location")
    private String location;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    
    @Column(name = "changed_who")
    private String changedWho;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_code")
    private String entityCode;

    @Column(name = "changed_when")
    private LocalDateTime changedWhen;
  }
}
