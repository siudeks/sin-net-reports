package sinnet;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import sinnet.models.ActionDuration;
import sinnet.models.Distance;
import sinnet.models.Email;
import sinnet.models.EntityValue;
import sinnet.models.Name;

/** Register details about provided service. */
@Value
@Builder(toBuilder = true)
public class ServiceValue implements EntityValue<ServiceValue> {

    /** Serviceman who did the service. */
    @Builder.Default
    private Email who = Email.empty();

    /** Date when the service has been provided. */
    private LocalDate when;

    /** Name of the Customer whom the service has been provided. */
    @Builder.Default
    private Name whom = Name.empty();

    /** Descriptive info what actually has been provided as the service. */
    private String what;

    /** How much time take overall activity related to provide the service. */
    @Builder.Default
    private ActionDuration howLong = ActionDuration.empty();

    /** How far is distance to the place where service has been provided. */
    @Builder.Default
    private Distance howFar = Distance.empty();
}
