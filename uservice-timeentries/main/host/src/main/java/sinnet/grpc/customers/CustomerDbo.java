package sinnet.grpc.customers;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import sinnet.grpc.customers.CustomerRepository.CustomerDboContact;
import sinnet.grpc.customers.CustomerRepository.CustomerDboSecret;
import sinnet.grpc.customers.CustomerRepository.CustomerDboSecretEx;

/**
 * TBD.
 */
@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
class CustomerDbo {

  @Column(name = "project_id")
  @EqualsAndHashCode.Include
  private UUID projectId;

  @Column(name = "entity_id")
  @Id
  @EqualsAndHashCode.Include
  private UUID entityId;

  @Column(name = "entity_version")
  @EqualsAndHashCode.Include
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

  @ElementCollection
  @CollectionTable(name = "contact", joinColumns = @JoinColumn(name = "customer_id"))
  private List<CustomerDboContact> contacts;

  @ElementCollection
  @CollectionTable(name = "secret", joinColumns = @JoinColumn(name = "customer_id"))
  private List<CustomerDboSecret> secrets;

  @ElementCollection
  @CollectionTable(name = "secret_ex", joinColumns = @JoinColumn(name = "customer_id"))
  private List<CustomerDboSecretEx> secretsEx;
}
