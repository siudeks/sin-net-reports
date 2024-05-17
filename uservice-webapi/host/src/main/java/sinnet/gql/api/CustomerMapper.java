package sinnet.gql.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.vavr.collection.Iterator;
import io.vavr.control.Option;
import sinnet.gql.models.CustomerContactGql;
import sinnet.gql.models.CustomerContactInputGql;
import sinnet.gql.models.CustomerEntityGql;
import sinnet.gql.models.CustomerInput;
import sinnet.gql.models.CustomerModelGql;
import sinnet.gql.models.CustomerSecretExGql;
import sinnet.gql.models.CustomerSecretExInput;
import sinnet.gql.models.CustomerSecretGql;
import sinnet.gql.models.CustomerSecretInput;
import sinnet.gql.models.EntityGql;
import sinnet.gql.models.SomeEntityGql;
import sinnet.gql.utils.PropsBuilder;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.customers.CustomerSecretEx;
import sinnet.grpc.customers.GetReply;

/** FixMe. */
public interface CustomerMapper extends CommonMapper {

  // DateTime, created and kept on server-side is UTC only. We can't send data to
  // client because GraphQL does not support Date / Time types.
  // So, we send a simple string without implicit contratc so that client may
  // convert them so its
  // proper models for date/time representation
  // reason: https://github.com/onlexnet/sin-net-reports/issues/59
  static DateTimeFormatter timestampFormatter = DateTimeFormatter.ISO_DATE_TIME;

  /** DocMe. */
  default LocalDateTime map(sinnet.grpc.customers.LocalDateTime it) {
    if (it == null) {
      return null;
    }
    return LocalDateTime.of(it.getYear(), it.getMonth(), it.getDay(), it.getHour(), it.getMinute(), it.getSecond());
  }

  /** Dox me. */
  default EntityId toGrpc(EntityGql item) {
    return EntityId.newBuilder()
      .setEntityId(item.getEntityId())
      .setProjectId(item.getProjectId())
      .setEntityVersion(item.getEntityVersion())
      .build();
  }

  /** Dox me. */
  default sinnet.grpc.customers.CustomerValue toGrpc(CustomerInput item) {
    if (item == null) {
      return null;
    }
    return PropsBuilder.build(sinnet.grpc.customers.CustomerValue.newBuilder())
      .set(b -> b::setOperatorEmail, item.getOperatorEmail())
      .set(b -> b::setBillingModel, item.getBillingModel())
      .set(b -> b::setSupportStatus, item.getSupportStatus())
      .set(b -> b::setDistance, item.getDistance())
      .set(b -> b::setCustomerName, item.getCustomerName())
      .set(b -> b::setCustomerCityName, item.getCustomerCityName())
      .set(b -> b::setCustomerAddress, item.getCustomerAddress())
      .set(b -> b::setNfzUmowa, item.getNfzUmowa())
      .set(b -> b::setNfzMaFilie, item.getNfzMaFilie())
      .set(b -> b::setNfzLekarz, item.getNfzLekarz())
      .set(b -> b::setNfzPolozna, item.getNfzPolozna())
      .set(b -> b::setNfzPielegniarkaSrodowiskowa, item.getNfzPielegniarkaSrodowiskowa())
      .set(b -> b::setNfzMedycynaSzkolna, item.getNfzMedycynaSzkolna())
      .set(b -> b::setNfzTransportSanitarny, item.getNfzTransportSanitarny())
      .set(b -> b::setNfzNocnaPomocLekarska, item.getNfzNocnaPomocLekarska())
      .set(b -> b::setNfzAmbulatoryjnaOpiekaSpecjalistyczna, item.getNfzAmbulatoryjnaOpiekaSpecjalistyczna())
      .set(b -> b::setNfzRehabilitacja, item.getNfzRehabilitacja())
      .set(b -> b::setNfzStomatologia, item.getNfzStomatologia())
      .set(b -> b::setNfzPsychiatria, item.getNfzPsychiatria())
      .set(b -> b::setNfzSzpitalnictwo, item.getNfzSzpitalnictwo())
      .set(b -> b::setNfzProgramyProfilaktyczne, item.getNfzProgramyProfilaktyczne())
      .set(b -> b::setNfzZaopatrzenieOrtopedyczne, item.getNfzZaopatrzenieOrtopedyczne())
      .set(b -> b::setNfzOpiekaDlugoterminowa, item.getNfzOpiekaDlugoterminowa())
      .set(b -> b::setNfzNotatki, item.getNfzNotatki())
      .set(b -> b::setKomercjaJest, item.getKomercjaJest())
      .set(b -> b::setKomercjaNotatki, item.getKomercjaNotatki())
      .set(b -> b::setDaneTechniczne, item.getDaneTechniczne())
      .done().build();
  }

  /** Fixme. */
  default sinnet.grpc.customers.CustomerSecretEx toGrpc(CustomerSecretExInput it, LocalDateTime whenChanged, String whoChanged) {
    if (it == null) {
      return null;
    }

    return PropsBuilder.build(sinnet.grpc.customers.CustomerSecretEx.newBuilder())
        .set(b -> b::setLocation, it.getLocation())
        .set(b -> b::setUsername, it.getUsername())
        .set(b -> b::setPassword, it.getPassword())
        .set(b -> b::setEntityName, it.getEntityName())
        .set(b -> b::setEntityCode, it.getEntityCode())
        .set(b -> b::setChangedWhen, CommonMapper.toGrpc(whenChanged))
        .set(b -> b::setOtpSecret, it.getOtpSecret())
        .set(b -> b::setOtpRecoveryKeys, it.getOtpRecoveryKeys())
        .set(b -> b::setChangedWho, whoChanged)
        .done().build();
  }

  /** Doc me. */
  default sinnet.grpc.customers.CustomerSecret toGrpc(CustomerSecretInput it, LocalDateTime whenChanged, String whoChanged) {
    if (it == null) {
      return null;
    }
    return PropsBuilder.build(sinnet.grpc.customers.CustomerSecret.newBuilder())
        .set(b -> b::setLocation, it.getLocation())
        .set(b -> b::setUsername, it.getUsername())
        .set(b -> b::setPassword, it.getPassword())
        .set(b -> b::setChangedWhen, CommonMapper.toGrpc(whenChanged))
        .set(b -> b::setChangedWho, whoChanged)
        .set(b -> b::setOtpSecret, it.getOtpSecret())
        .set(b -> b::setOtpRecoveryKeys, it.getOtpRecoveryKeys())
        .done().build();
  }

  /** Fixme. */
  default sinnet.grpc.customers.CustomerContact toGrpc(CustomerContactInputGql it) {
    if (it == null) {
      return null;
    }
    return PropsBuilder.build(sinnet.grpc.customers.CustomerContact.newBuilder())
        .set(b -> b::setFirstName, it.getFirstName())
        .set(b -> b::setLastName, it.getLastName())
        .set(b -> b::setPhoneNo, it.getPhoneNo())
        .set(b -> b::setEmail, it.getEmail())
        .done().build();
  }
  
  /** Doc me. */
  default SomeEntityGql toGql(EntityId id) {
    if (id == null) {
      return null;
    }
    return new SomeEntityGql()
      .setEntityId(id.getEntityId())
      .setEntityVersion(id.getEntityVersion())
      .setProjectId(id.getProjectId());
  }

  /** Fixme. */
  default CustomerContactGql toGql(sinnet.grpc.customers.CustomerContact it) {
    if (it == null) {
      return null;
    }
    var result = new CustomerContactGql();
    result.setFirstName(it.getFirstName());
    result.setLastName(it.getLastName());
    result.setPhoneNo(it.getPhoneNo());
    result.setEmail(it.getEmail());      
    return result;
  }
  
  /** DocMe. */
  default CustomerSecretExGql toGql(CustomerSecretEx it) {
    if (it == null) {
      return null;
    }
    var result = new CustomerSecretExGql();
    result.setLocation(it.getLocation());
    result.setUsername(it.getUsername());
    result.setPassword(it.getPassword());
    result.setEntityCode(it.getEntityCode());
    result.setEntityName(it.getEntityName());
    result.setChangedWhen(Option.of(map(it.getChangedWhen())).map(timestampFormatter::format).getOrElse("?"));
    result.setChangedWho(it.getChangedWho());
    result.setOtpSecret(it.getOtpSecret());
    result.setOtpRecoveryKeys(it.getOtpRecoveryKeys());
    return result;
  }

  /** DocMe. */
  default CustomerSecretGql toGql(sinnet.grpc.customers.CustomerSecret it) {
    if (it == null) {
      return null;
    }
    var result = new CustomerSecretGql();
    result.setLocation(it.getLocation());
    result.setUsername(it.getUsername());
    result.setPassword(it.getPassword());
    result.setChangedWhen(Option.of(map(it.getChangedWhen())).map(timestampFormatter::format).getOrElse("?"));
    result.setChangedWho(it.getChangedWho());
    result.setOtpSecret(it.getOtpSecret());
    result.setOtpRecoveryKeys(it.getOtpRecoveryKeys());
    return result;
  }

  /** FixMe. */
  default CustomerEntityGql toGql(sinnet.grpc.customers.CustomerModel item) {
    if (item == null) {
      return null;
    }
    var result = new CustomerEntityGql();
    result.setId(toGql(item.getId()));
    result.setData(toGql(item.getValue()));
    result.setSecrets(Iterator.ofAll(item.getSecretsList()).map(this::toGql).toJavaArray(CustomerSecretGql[]::new));
    result.setSecretsEx(Iterator.ofAll(item.getSecretExList()).map(this::toGql).toJavaArray(CustomerSecretExGql[]::new));
    result.setContacts(Iterator.ofAll(item.getContactsList()).map(this::toGql).toJavaArray(CustomerContactGql[]::new));
    return result;
  }
  
  /** FixMe. */
  default CustomerModelGql toGql(sinnet.grpc.customers.CustomerValue item) {
    if (item == null) {
      return null;
    }
    var it = new CustomerModelGql();
    it.setOperatorEmail(item.getOperatorEmail());
    it.setBillingModel(item.getBillingModel());
    it.setSupportStatus(item.getSupportStatus());
    it.setDistance(item.getDistance());
    it.setCustomerName(item.getCustomerName());
    it.setCustomerCityName(item.getCustomerCityName());
    it.setCustomerAddress(item.getCustomerAddress());
    it.setNfzUmowa(item.getNfzUmowa());
    it.setNfzMaFilie(item.getNfzMaFilie());
    it.setNfzLekarz(item.getNfzLekarz());
    it.setNfzPolozna(item.getNfzPolozna());
    it.setNfzPielegniarkaSrodowiskowa(item.getNfzPielegniarkaSrodowiskowa());
    it.setNfzMedycynaSzkolna(item.getNfzMedycynaSzkolna());
    it.setNfzTransportSanitarny(item.getNfzTransportSanitarny());
    it.setNfzNocnaPomocLekarska(item.getNfzNocnaPomocLekarska());
    it.setNfzAmbulatoryjnaOpiekaSpecjalistyczna(item.getNfzAmbulatoryjnaOpiekaSpecjalistyczna());
    it.setNfzRehabilitacja(item.getNfzRehabilitacja());
    it.setNfzStomatologia(item.getNfzStomatologia());
    it.setNfzPsychiatria(item.getNfzPsychiatria());
    it.setNfzSzpitalnictwo(item.getNfzSzpitalnictwo());
    it.setNfzProgramyProfilaktyczne(item.getNfzProgramyProfilaktyczne());
    it.setNfzZaopatrzenieOrtopedyczne(item.getNfzZaopatrzenieOrtopedyczne());
    it.setNfzOpiekaDlugoterminowa(item.getNfzOpiekaDlugoterminowa());
    it.setNfzNotatki(item.getNfzNotatki());
    it.setKomercjaJest(item.getKomercjaJest());
    it.setKomercjaNotatki(item.getKomercjaNotatki());
    it.setDaneTechniczne(item.getDaneTechniczne());
    return it;
  }
   
  /** FixMe. */
  default CustomerEntityGql toGql(GetReply dto) {
    if (dto == null) {
      return null;
    }
    var item = dto.getModel();
    return toGql(item);
  }

}
