package sinnet.gql.customers;

import org.springframework.stereotype.Component;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import sinnet.grpc.RpcCommandHandler;
import sinnet.grpc.customers.UpdateCommand;
import sinnet.grpc.customers.UpdateResult;
import sinnet.models.CustomerSecret;
import sinnet.models.CustomerValue;
import sinnet.models.Email;
import sinnet.models.ShardedId;
import sinnet.models.Name;

@Component
@Slf4j
public class CustomersRpcUpdate implements
                                RpcCommandHandler<UpdateCommand, UpdateResult>,
                                MapperDto {
                                   

  @Override
  public UpdateResult apply(UpdateCommand cmd) {
    var emailOfRequestor = Email.of(cmd.getUserToken().getRequestorEmail());
    var entry = cmd.getModel().getValue();
    var eid = fromDto(cmd.getModel().getId());
    var model = fromDto(cmd.getModel())
        .operatorEmail(entry.getOperatorEmail())
        .supportStatus(entry.getSupportStatus())
        .billingModel(entry.getBillingModel())
        .distance(entry.getDistance())
        .customerName(Name.of(entry.getCustomerName()))
        .customerCityName(Name.of(entry.getCustomerCityName()))
        .customerAddress(entry.getCustomerAddress())
        .nfzUmowa(entry.getNfzUmowa())
        .nfzMaFilie(entry.getNfzMaFilie())
        .nfzLekarz(entry.getNfzLekarz())
        .nfzPolozna(entry.getNfzPolozna())
        .nfzPielegniarkaSrodowiskowa(entry.getNfzPielegniarkaSrodowiskowa())
        .nfzMedycynaSzkolna(entry.getNfzMedycynaSzkolna())
        .nfzTransportSanitarny(entry.getNfzTransportSanitarny())
        .nfzNocnaPomocLekarska(entry.getNfzNocnaPomocLekarska())
        .nfzAmbulatoryjnaOpiekaSpecjalistyczna(entry.getNfzAmbulatoryjnaOpiekaSpecjalistyczna())
        .nfzRehabilitacja(entry.getNfzRehabilitacja())
        .nfzStomatologia(entry.getNfzStomatologia())
        .nfzPsychiatria(entry.getNfzPsychiatria())
        .nfzSzpitalnictwo(entry.getNfzSzpitalnictwo())
        .nfzProgramyProfilaktyczne(entry.getNfzProgramyProfilaktyczne())
        .nfzZaopatrzenieOrtopedyczne(entry.getNfzZaopatrzenieOrtopedyczne())
        .nfzOpiekaDlugoterminowa(entry.getNfzOpiekaDlugoterminowa())
        .nfzNotatki(entry.getNfzNotatki())
        .komercjaJest(entry.getKomercjaJest())
        .komercjaNotatki(entry.getKomercjaNotatki())
        .daneTechniczne(entry.getDaneTechniczne());
    var secrets = cmd.getModel().getSecretsList();
    var mSecrets = secrets.stream()
        .map(it -> new CustomerSecret()
            .setLocation(it.getLocation())
            .setUsername(it.getUsername())
            .setPassword(it.getPassword()))
        .toArray(CustomerSecret[]::new);
    var secretsEx = cmd.getModel().getSecretExList();
    var mSecretsEx = secretsEx.stream()
        .map(it -> ChangeCustomerData.SecretEx.builder()
            .location(it.getLocation())
            .username(it.getUsername())
            .password(it.getPassword())
            .entityName(it.getEntityName())
            .entityCode(it.getEntityCode())
            .build())
        .toArray(ChangeCustomerData.SecretEx[]::new);
    var contacts = cmd.getModel().getContactsList();
    var mContacts = contacts.stream()
        .map(it -> ChangeCustomerData.Contact.builder()
            .firstName(it.getFirstName())
            .lastName(it.getLastName())
            .phoneNo(it.getPhoneNo())
            .email(it.getEmail())
            .build())
        .toArray(ChangeCustomerData.Contact[]::new);

    var mCmd = ChangeCustomerData.Command.builder()
        .requestor(emailOfRequestor)
        .id(eid)
        .value(value)
        .secrets(mSecrets)
        .secretsEx(mSecretsEx)
        .contacts(mContacts)
        .build();

    super.ask(mCmd).onComplete(Handlers.logged(log, streamObserver, it -> {
        var result = toDto(it);
        return UpdateResult.newBuilder()
            .setEntityId(result)
            .build();
    }));
  }

}

