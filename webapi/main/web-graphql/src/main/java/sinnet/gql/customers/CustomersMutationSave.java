package sinnet.gql.customers;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLResolver;
import io.vertx.core.eventbus.EventBus;
import sinnet.IdentityProvider;
import sinnet.gql.MyEntity;
import sinnet.gql.SomeEntity;
import sinnet.bus.AskTemplate;
import sinnet.bus.commands.ChangeCustomerData;
import sinnet.models.CustomerContact;
import sinnet.models.CustomerValue;
import sinnet.models.Email;
import sinnet.models.EntityId;
import sinnet.models.Name;

@Component
public class CustomersMutationSave extends AskTemplate<ChangeCustomerData.Command, EntityId>
                                   implements GraphQLResolver<CustomersMutation> {

  @Autowired
  private IdentityProvider identityProvider;

  public CustomersMutationSave(EventBus eventBus) {
    super(ChangeCustomerData.Command.ADDRESS, EntityId.class, eventBus);
  }

  CompletableFuture<SomeEntity> save(CustomersMutation gcontext, MyEntity id, CustomerInput entry,
                                                                              CustomerSecret[] secrets,
                                                                              CustomerSecretEx[] secretsEx,
                                                                              CustomerContact[] contacts) {
    var maybeRequestor = identityProvider.getCurrent();
    if (!maybeRequestor.isPresent()) {
      throw new GraphQLException("Access denied");
    }

    if (!Objects.equals(id.getProjectId(), gcontext.getProjectId())) {
      throw new GraphQLException("Invalid project id");
    }

    var eid = EntityId.of(id.getProjectId(), id.getEntityId(), id.getEntityVersion());
    var value = CustomerValue.builder()
        .operatorEmail(entry.getOperatorEmail())
        .supportStatus(entry.getSupportStatus())
        .billingModel(entry.getBillingModel())
        .distance(entry.getDistance())
        .customerName(Name.of(entry.getCustomerName()))
        .customerCityName(Name.of(entry.getCustomerCityName()))
        .customerAddress(entry.getCustomerAddress())
        .nfzUmowa(entry.isNfzUmowa())
        .nfzMaFilie(entry.isNfzMaFilie())
        .nfzLekarz(entry.isNfzLekarz())
        .nfzPolozna(entry.isNfzPolozna())
        .nfzPielegniarkaSrodowiskowa(entry.isNfzPielegniarkaSrodowiskowa())
        .nfzMedycynaSzkolna(entry.isNfzMedycynaSzkolna())
        .nfzTransportSanitarny(entry.isNfzTransportSanitarny())
        .nfzNocnaPomocLekarska(entry.isNfzNocnaPomocLekarska())
        .nfzAmbulatoryjnaOpiekaSpecjalistyczna(entry.isNfzAmbulatoryjnaOpiekaSpecjalistyczna())
        .nfzRehabilitacja(entry.isNfzRehabilitacja())
        .nfzStomatologia(entry.isNfzStomatologia())
        .nfzPsychiatria(entry.isNfzPsychiatria())
        .nfzSzpitalnictwo(entry.isNfzSzpitalnictwo())
        .nfzProgramyProfilaktyczne(entry.isNfzProgramyProfilaktyczne())
        .nfzZaopatrzenieOrtopedyczne(entry.isNfzZaopatrzenieOrtopedyczne())
        .nfzOpiekaDlugoterminowa(entry.isNfzOpiekaDlugoterminowa())
        .nfzNotatki(entry.getNfzNotatki())
        .komercjaJest(entry.isKomercjaJest())
        .komercjaNotatki(entry.getKomercjaNotatki())
        .daneTechniczne(entry.getDaneTechniczne())
        .build();
    var mSecrets = Arrays
        .stream(secrets)
        .map(it -> ChangeCustomerData.Secret.builder()
            .location(it.getLocation())
            .username(it.getUsername())
            .password(it.getPassword())
            .build())
        .toArray(ChangeCustomerData.Secret[]::new);
    var mSecretsEx = Arrays
        .stream(secretsEx)
        .map(it -> ChangeCustomerData.SecretEx.builder()
            .location(it.getLocation())
            .username(it.getUsername())
            .password(it.getPassword())
            .entityName(it.getEntityName())
            .entityCode(it.getEntityCode())
            .build())
        .toArray(ChangeCustomerData.SecretEx[]::new);
    var mContacts = Arrays
        .stream(contacts)
        .map(it -> ChangeCustomerData.Contact.builder()
            .firstName(it.getFirstName())
            .lastName(it.getLastName())
            .phoneNo(it.getPhoneNo())
            .email(it.getEmail())
            .build())
        .toArray(ChangeCustomerData.Contact[]::new);

    var emailOfRequestor = maybeRequestor.get().getEmail();
    var cmd = ChangeCustomerData.Command.builder()
        .requestor(Email.of(emailOfRequestor))
        .id(eid)
        .value(value)
        .secrets(mSecrets)
        .secretsEx(mSecretsEx)
        .contacts(mContacts)
        .build();
    return super.ask(cmd).thenApply(m -> new SomeEntity(m.getProjectId(), m.getId(), m.getVersion()));
  }
}

