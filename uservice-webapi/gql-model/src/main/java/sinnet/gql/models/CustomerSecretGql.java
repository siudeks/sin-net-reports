package sinnet.gql.models;

import lombok.Data;

/** Fixme. */
@Data
public class CustomerSecretGql {
  private String location;
  private String username;
  private String password;
  private String changedWhen;
  private String changedWho;
  private String otpSecret;
  private String otpRecoveryKeys;
}
