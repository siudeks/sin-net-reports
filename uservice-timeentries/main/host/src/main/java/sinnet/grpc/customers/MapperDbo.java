package sinnet.grpc.customers;

import sinnet.models.CustomerContact;
import sinnet.models.CustomerModel;
import sinnet.models.CustomerSecret;
import sinnet.models.CustomerSecretEx;
import sinnet.models.CustomerValue;
import sinnet.models.ValName;
import sinnet.models.ValEmail;
import sinnet.models.ShardedId;

/**
 * TBD.
 */
public interface MapperDbo {

  /**
   * TBD.
   */
  default CustomerContact fromDbo(CustomerRepository.CustomerDboContact dbo) {
    return new CustomerContact()
      .setFirstName(dbo.getFirstName())
      .setLastName(dbo.getLastName())
      .setPhoneNo(dbo.getPhoneNo())
      .setEmail(dbo.getEmail());
  }

  /**
   * TBD.
   */
  default CustomerSecretEx fromDbo(CustomerRepository.CustomerDboSecretEx dbo) {
    return new CustomerSecretEx()
      .setUsername(dbo.getUsername())
      .setPassword(dbo.getPassword())
      .setLocation(dbo.getLocation())
      .setEntityName(dbo.getEntityName())
      .setEntityCode(dbo.getEntityCode())
      .setChangedWho(ValEmail.of(dbo.getChangedWho()))
      .setChangedWhen(dbo.getChangedWhen());
  }

  /**
   * TBD.
   */
  default CustomerSecret fromDbo(CustomerRepository.CustomerDboSecret dbo) {
    return new CustomerSecret()
      .setUsername(dbo.getUsername())
      .setPassword(dbo.getPassword())
      .setLocation(dbo.getLocation())
      .setChangedWho(ValEmail.of(dbo.getChangedWho()))
      .setChangedWhen(dbo.getChangedWhen());
  }

  /**
   * TBD.
   */
  default CustomerModel fromDbo(CustomerDbo dbo) {
    var id = ShardedId.of(dbo.getProjectId(), dbo.getEntityId(), dbo.getEntityVersion());
    var contacts = dbo.getContacts().stream().map(this::fromDbo).toList();
    var secrets = dbo.getSecrets().stream().map(this::fromDbo).toList();
    var secretsEx = dbo.getSecretsEx().stream().map(this::fromDbo).toList();
    var value = new CustomerValue()
        .operatorEmail(ValEmail.of(dbo.getOperatorEmail()))
        .billingModel(dbo.getBillingModel())
        .supportStatus(dbo.getSupportStatus())
        .distance(dbo.getDistance())
        .customerName(ValName.of(dbo.getCustomerName()))
        .customerCityName(ValName.of(dbo.getCustomerCityName()))
        .customerAddress(dbo.getCustomerAddress())
        .nfzUmowa(dbo.getNfzUmowa())
        .nfzMaFilie(dbo.getNfzMaFilie())
        .nfzLekarz(dbo.getNfzLekarz())
        .nfzPolozna(dbo.getNfzPolozna())
        .nfzPielegniarkaSrodowiskowa(dbo.getNfzPielegniarkaSrodowiskowa())
        .nfzMedycynaSzkolna(dbo.getNfzMedycynaSzkolna())
        .nfzTransportSanitarny(dbo.getNfzTransportSanitarny())
        .nfzNocnaPomocLekarska(dbo.getNfzNocnaPomocLekarska())
        .nfzAmbulatoryjnaOpiekaSpecjalistyczna(dbo.getNfzAmbulatoryjnaOpiekaSpecjalistyczna())
        .nfzRehabilitacja(dbo.getNfzRehabilitacja())
        .nfzStomatologia(dbo.getNfzStomatologia())
        .nfzPsychiatria(dbo.getNfzPsychiatria())
        .nfzSzpitalnictwo(dbo.getNfzSzpitalnictwo())
        .nfzProgramyProfilaktyczne(dbo.getNfzProgramyProfilaktyczne())
        .nfzZaopatrzenieOrtopedyczne(dbo.getNfzZaopatrzenieOrtopedyczne())
        .nfzOpiekaDlugoterminowa(dbo.getNfzOpiekaDlugoterminowa())
        .nfzNotatki(dbo.getNfzNotatki())
        .komercjaJest(dbo.getKomercjaJest())
        .komercjaNotatki(dbo.getKomercjaNotatki())
        .daneTechniczne(dbo.getDaneTechniczne());
    return new CustomerModel().setId(id).setValue(value).setContacts(contacts).setSecrets(secrets).setSecretsEx(secretsEx);
  }

}
