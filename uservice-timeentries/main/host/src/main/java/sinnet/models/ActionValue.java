package sinnet.models;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

/** Register details about provided service. */
@Data
@Accessors(chain = true)
public class ActionValue implements EntityValue<ActionValue> {

  /** Serviceman who did the service. */
  private ValEmail who = ValEmail.empty();

  /** Date when the service has been provided. */
  private LocalDate when;

  /** Id of the Customer whom the service has been provided. */
  private UUID whom;

  /** Descriptive info what actually has been provided as the service. */
  private String what;

  /** How much time take overall activity related to provide the service. */
  private ActionDuration howLong = ActionDuration.empty();

  /** How far is distance to the place where service has been provided. */
  private Distance howFar = Distance.empty();
}
