package sinnet.grpc.customers;

import java.time.LocalDateTime;
import java.util.Optional;

import io.vavr.control.Option;
import sinnet.grpc.common.Mapper;
import sinnet.grpc.mapping.PropsBuilder;
import sinnet.models.CustomerContact;
import sinnet.models.CustomerModel;
import sinnet.models.CustomerSecret;
import sinnet.models.CustomerSecretEx;
import sinnet.models.CustomerValue;
import sinnet.models.ValEmail;
import sinnet.models.ValName;

/**
 * Doman <-> Dto translations.
 */
public interface MapperDto extends Mapper {

  /**
   * TBD.
   */
  default sinnet.grpc.customers.CustomerModel toDto(CustomerModel it) {
    return sinnet.grpc.customers.CustomerModel.newBuilder()
        .setId(PropsBuilder.build(sinnet.grpc.common.EntityId.newBuilder())
            .set(it.getId().getProjectId().toString(), b -> b::setProjectId)
            .set(it.getId().getId().toString(), b -> b::setEntityId)
            .set(it.getId().getVersion(), b -> b::setEntityVersion)
            .done().build())
        .setValue(toDto(it.getValue()))
        .addAllSecrets(it.getSecrets().stream().map(this::toDto).toList())
        .addAllSecretEx(it.getSecretsEx().stream().map(this::toDto).toList())
        .addAllContacts(it.getContacts().stream().map(this::toDto).toList())
        .build();
  }

  /**
   * TBD.
   */
  default sinnet.grpc.customers.CustomerValue toDto(CustomerValue value) {
    return PropsBuilder.build(sinnet.grpc.customers.CustomerValue.newBuilder())
        .set(value.operatorEmail(), ValEmail::getValue, b -> b::setOperatorEmail)
        .set(value.billingModel(), b -> b::setBillingModel)
        .set(value.supportStatus(), b -> b::setSupportStatus)
        .set(value.distance(), b -> b::setDistance)
        .set(value.customerName().getValue(), b -> b::setCustomerName)
        .set(value.customerCityName().getValue(), b -> b::setCustomerCityName)
        .set(value.customerAddress(), b -> b::setCustomerAddress)
        .set(value.nfzUmowa(), b -> b::setNfzUmowa)
        .set(value.nfzMaFilie(), b -> b::setNfzMaFilie)
        .set(value.nfzLekarz(), b -> b::setNfzLekarz)
        .set(value.nfzPolozna(), b -> b::setNfzPolozna)
        .set(value.nfzPielegniarkaSrodowiskowa(), b -> b::setNfzPielegniarkaSrodowiskowa)
        .set(value.nfzMedycynaSzkolna(), b -> b::setNfzMedycynaSzkolna)
        .set(value.nfzTransportSanitarny(), b -> b::setNfzTransportSanitarny)
        .set(value.nfzNocnaPomocLekarska(), b -> b::setNfzNocnaPomocLekarska)
        .set(value.nfzAmbulatoryjnaOpiekaSpecjalistyczna(), b -> b::setNfzAmbulatoryjnaOpiekaSpecjalistyczna)
        .set(value.nfzRehabilitacja(), b -> b::setNfzRehabilitacja)
        .set(value.nfzStomatologia(), b -> b::setNfzStomatologia)
        .set(value.nfzPsychiatria(), b -> b::setNfzPsychiatria)
        .set(value.nfzSzpitalnictwo(), b -> b::setNfzSzpitalnictwo)
        .set(value.nfzProgramyProfilaktyczne(), b -> b::setNfzProgramyProfilaktyczne)
        .set(value.nfzZaopatrzenieOrtopedyczne(), b -> b::setNfzZaopatrzenieOrtopedyczne)
        .set(value.nfzOpiekaDlugoterminowa(), b -> b::setNfzOpiekaDlugoterminowa)
        .set(value.nfzNotatki(), b -> b::setNfzNotatki)
        .set(value.komercjaJest(), b -> b::setKomercjaJest)
        .set(value.komercjaNotatki(), b -> b::setKomercjaNotatki)
        .set(value.daneTechniczne(), b -> b::setDaneTechniczne)
        .done().build();
  }

  private sinnet.grpc.customers.CustomerContact toDto(CustomerContact it) {
    return PropsBuilder.build(sinnet.grpc.customers.CustomerContact.newBuilder())
        .set(it.getFirstName(), b -> b::setFirstName)
        .set(it.getLastName(), b -> b::setLastName)
        .set(it.getPhoneNo(), b -> b::setPhoneNo)
        .set(it.getEmail(), b -> b::setEmail)
        .done().build();
  }

  private static sinnet.grpc.customers.LocalDateTime toDto(java.time.LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return sinnet.grpc.customers.LocalDateTime.newBuilder()
        .setYear(dateTime.getYear())
        .setMonth(dateTime.getMonthValue())
        .setDay(dateTime.getDayOfMonth())
        .setHour(dateTime.getHour())
        .setMinute(dateTime.getMinute())
        .setSecond(dateTime.getSecond())
        .build();
  }

  private sinnet.grpc.customers.CustomerSecret toDto(CustomerSecret value) {
    return PropsBuilder.build(sinnet.grpc.customers.CustomerSecret.newBuilder())
        .set(Option.of(value.getLocation()).getOrElse("?"), b -> b::setLocation)
        .set(value.getUsername(), b -> b::setUsername)
        .set(value.getPassword(), b -> b::setPassword)
        .set(Option.of(value.getChangedWho().getValue()).getOrElse("?"), b -> b::setChangedWho)
        .set(toDto(value.getChangedWhen()), b -> b::setChangedWhen)
        .done().build();
  }

  private sinnet.grpc.customers.CustomerSecretEx toDto(CustomerSecretEx it) {
    return PropsBuilder.build(sinnet.grpc.customers.CustomerSecretEx.newBuilder())
        .set(Option.of(it.getLocation()).getOrElse("?"), b -> b::setLocation)
        .set(it.getUsername(), b -> b::setUsername)
        .set(it.getPassword(), b -> b::setPassword)
        .set(it.getEntityName(), b -> b::setEntityName)
        .set(it.getEntityCode(), b -> b::setEntityCode)
        .set(Option.of(it.getChangedWho().getValue()).getOrElse("?"), b -> b::setChangedWho)
        .set(toDto(it.getChangedWhen()), b -> b::setChangedWhen)
        .done().build();
  }

  /**
   * TBD.
   */
  default LocalDateTime fromDto(sinnet.grpc.customers.LocalDateTime item) {
    return Optional.ofNullable(item)
        .map(it -> LocalDateTime.of(it.getYear(), it.getMonth(), it.getDay(), it.getHour(), it.getMinute(),
            it.getSecond()))
        .orElse(null);
  }

  /**
   * TBD.
   */
  default CustomerContact fromDto(sinnet.grpc.customers.CustomerContact item) {
    return new CustomerContact()
        .setFirstName(item.getFirstName())
        .setLastName(item.getLastName())
        .setPhoneNo(item.getPhoneNo())
        .setEmail(item.getEmail());
  }

  /**
   * TBD.
   */
  default CustomerSecret fromDto(sinnet.grpc.customers.CustomerSecret item) {
    return new CustomerSecret()
        .setLocation(item.getLocation())
        .setPassword(item.getPassword())
        .setUsername(item.getUsername())
        .setChangedWho(ValEmail.of(item.getChangedWho()))
        .setChangedWhen(fromDto(item.getChangedWhen()));
  }

  /**
   * TBD.
   */
  default CustomerSecretEx fromDto(sinnet.grpc.customers.CustomerSecretEx item) {
    return new CustomerSecretEx()
        .setLocation(item.getLocation())
        .setPassword(item.getPassword())
        .setUsername(item.getUsername())
        .setEntityCode(item.getEntityCode())
        .setEntityName(item.getEntityName())
        .setChangedWho(ValEmail.of(item.getChangedWho()))
        .setChangedWhen(fromDto(item.getChangedWhen()));
  }

  /**
   * TBD.
   */
  default CustomerModel fromDto(sinnet.grpc.customers.CustomerModel item) {
    return new CustomerModel()
        .setId(fromDto(item.getId()))
        .setValue(fromDto(item.getValue()))
        .setContacts(item.getContactsList().stream().map(this::fromDto).toList())
        .setSecrets(item.getSecretsList().stream().map(this::fromDto).toList())
        .setSecretsEx(item.getSecretExList().stream().map(this::fromDto).toList());
  }

  /**
   * TBD.
   */
  default CustomerValue fromDto(sinnet.grpc.customers.CustomerValue dto) {
    if (dto == null) {
      return null;
    }
    return new CustomerValue()
        .operatorEmail(ValEmail.of(dto.getOperatorEmail()))
        .supportStatus(dto.getSupportStatus())
        .billingModel(dto.getBillingModel())
        .distance(dto.getDistance())
        .customerName(ValName.of(dto.getCustomerName()))
        .customerCityName(ValName.of(dto.getCustomerCityName()))
        .customerAddress(dto.getCustomerAddress())
        .nfzUmowa(dto.getNfzUmowa())
        .nfzMaFilie(dto.getNfzMaFilie())
        .nfzLekarz(dto.getNfzLekarz())
        .nfzPolozna(dto.getNfzPolozna())
        .nfzPielegniarkaSrodowiskowa(dto.getNfzPielegniarkaSrodowiskowa())
        .nfzMedycynaSzkolna(dto.getNfzMedycynaSzkolna())
        .nfzTransportSanitarny(dto.getNfzTransportSanitarny())
        .nfzNocnaPomocLekarska(dto.getNfzNocnaPomocLekarska())
        .nfzAmbulatoryjnaOpiekaSpecjalistyczna(dto.getNfzAmbulatoryjnaOpiekaSpecjalistyczna())
        .nfzRehabilitacja(dto.getNfzRehabilitacja())
        .nfzStomatologia(dto.getNfzStomatologia())
        .nfzPsychiatria(dto.getNfzPsychiatria())
        .nfzSzpitalnictwo(dto.getNfzSzpitalnictwo())
        .nfzProgramyProfilaktyczne(dto.getNfzProgramyProfilaktyczne())
        .nfzZaopatrzenieOrtopedyczne(dto.getNfzZaopatrzenieOrtopedyczne())
        .nfzOpiekaDlugoterminowa(dto.getNfzOpiekaDlugoterminowa())
        .nfzNotatki(dto.getNfzNotatki())
        .komercjaJest(dto.getKomercjaJest())
        .komercjaNotatki(dto.getKomercjaNotatki())
        .daneTechniczne(dto.getDaneTechniczne());
  }

}
