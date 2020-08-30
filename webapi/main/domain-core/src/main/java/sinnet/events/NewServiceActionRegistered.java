package sinnet.events;

import java.time.LocalDate;

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
    private String id;

    /** Date when the service has been provided. */
    private LocalDate when;
    /** What has been done as the service - tekstual description. */
    private String description;
}
