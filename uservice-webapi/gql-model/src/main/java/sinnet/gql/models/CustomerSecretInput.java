package sinnet.gql.models;

import lombok.Data;
import lombok.experimental.Accessors;

/** Fixme. */
@Data
@Accessors(chain = true)
public class CustomerSecretInput {
  private String location;
  private String username;
  private String password;
  private String otpSecret;
  private String otpRecoveryKeys;
}
