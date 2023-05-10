package sinnet.models;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TBD.
 */
@Data
@Accessors(chain = true)
public final class CustomerContact {
  private String firstName;
  private String lastName;
  private String phoneNo;
  private String email;
}
