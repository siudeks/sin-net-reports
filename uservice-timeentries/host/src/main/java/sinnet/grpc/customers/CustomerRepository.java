package sinnet.grpc.customers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * TBD.
 */
@Repository
@Transactional(TxType.MANDATORY)
public interface CustomerRepository extends JpaRepository<CustomerRepository.CustomerDbo, UUID> {

  void deleteByProjectIdAndEntityIdAndEntityVersion(UUID projectId, UUID entityId, Long version);

  @Query("""
      select c from CustomerDbo c
      left join fetch c.secrets
      left join fetch c.secretsEx
      left join fetch c.contacts
      where c.projectId = :projectId
      """)
  List<CustomerDbo> findByProjectId(UUID projectId);
  
  CustomerDbo findByProjectIdAndEntityId(UUID projectId, UUID entityId);

  @Override
  @Query("select c from CustomerDbo c left join fetch c.secrets")
  List<CustomerRepository.CustomerDbo> findAll();

  /**
   * TBD.
   */
  @Entity(name = "CustomerDbo")
  @Table(name = "customers")
  @Data
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  @Accessors(chain = true)
  class CustomerDbo {

    @Column(name = "project_id", columnDefinition = "uniqueidentifier")
    @EqualsAndHashCode.Include
    private UUID projectId;

    @Column(name = "entity_id", columnDefinition = "uniqueidentifier")
    @Id
    @EqualsAndHashCode.Include
    private UUID entityId;

    @Column(name = "entity_version")
    @EqualsAndHashCode.Include
    @Version
    private Long entityVersion;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_city_name")
    private String customerCityName;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "operator_email")
    private String operatorEmail;

    @Column(name = "billing_model")
    private String billingModel;

    @Column(name = "support_status")
    private String supportStatus;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "nfz_umowa")
    private Boolean nfzUmowa;

    @Column(name = "nfz_ma_filie")
    private Boolean nfzMaFilie;

    @Column(name = "nfz_lekarz")
    private Boolean nfzLekarz;

    @Column(name = "nfz_polozna")
    private Boolean nfzPolozna;

    @Column(name = "nfz_pielegniarka_srodowiskowa")
    private Boolean nfzPielegniarkaSrodowiskowa;

    @Column(name = "nfz_medycyna_szkolna")
    private Boolean nfzMedycynaSzkolna;

    @Column(name = "nfz_transport_sanitarny")
    private Boolean nfzTransportSanitarny;

    @Column(name = "nfz_nocna_pomoc_lekarska")
    private Boolean nfzNocnaPomocLekarska;

    @Column(name = "nfz_ambulatoryjna_opieka_specjalistyczna")
    private Boolean nfzAmbulatoryjnaOpiekaSpecjalistyczna;

    @Column(name = "nfz_rehabilitacja")
    private Boolean nfzRehabilitacja;

    @Column(name = "nfz_stomatologia")
    private Boolean nfzStomatologia;

    @Column(name = "nfz_psychiatria")
    private Boolean nfzPsychiatria;

    @Column(name = "nfz_szpitalnictwo")
    private Boolean nfzSzpitalnictwo;

    @Column(name = "nfz_programy_profilaktyczne")
    private Boolean nfzProgramyProfilaktyczne;

    @Column(name = "nfz_zaopatrzenie_ortopedyczne")
    private Boolean nfzZaopatrzenieOrtopedyczne;

    @Column(name = "nfz_opieka_dlugoterminowa")
    private Boolean nfzOpiekaDlugoterminowa;

    @Column(name = "nfz_notatki")
    private String nfzNotatki;

    @Column(name = "komercja_jest")
    private Boolean komercjaJest;

    @Column(name = "komercja_notatki")
    private String komercjaNotatki;

    @Column(name = "dane_techniczne")
    private String daneTechniczne;

    // Hack with FetchType.EAGER:
    // I observe thousend of individual requests for elements of such collection when CustomerDbo.loadAll is invoked
    // I hope SQL will be opttimized to load data faster or in more batch way
    // Correct approach: the entity should not be used when .loadAll is requested, especially with only sobe limited set of fields, especiallly
    // without children collections.
    // the same optimalization is about the rest of collection in the class
    // @ElementCollection(fetch = FetchType.EAGER)
    @ElementCollection
    @CollectionTable(name = "contact", joinColumns = @JoinColumn(name = "customer_id", columnDefinition = "uniqueidentifier"))
    private Set<CustomerDboContact> contacts;

    @ElementCollection
    @CollectionTable(name = "secret", joinColumns = @JoinColumn(name = "customer_id", columnDefinition = "uniqueidentifier"))
    private Set<CustomerDboSecret> secrets;

    @ElementCollection
    @CollectionTable(name = "secret_ex", joinColumns = @JoinColumn(name = "customer_id", columnDefinition = "uniqueidentifier"))
    private Set<CustomerDboSecretEx> secretsEx;
  }

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
  
    @Column(name = "otp_secret")
    private String otpSecret;

    @Column(name = "otp_recovery_keys")
    private String otpRecoveryKeys;
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

    @Column(name = "otp_secret")
    private String otpSecret;

    @Column(name = "otp_recovery_keys")
    private String otpRecoveryKeys;
  }
}
