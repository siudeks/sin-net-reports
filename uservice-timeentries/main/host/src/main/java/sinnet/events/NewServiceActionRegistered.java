package sinnet.events;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** New service has been just registered in the System. */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class NewServiceActionRegistered {
  /** Unique Service ID. */
  private UUID id;

  /** Name of the serviceman. */
  private String servicemanName;
  /** Name of the customer. */
  private String customerName;
  /** Date when the service has been provided. */
  private LocalDate when;
  /** What has been done as the service - tekstual description. */
  private String description;
}
