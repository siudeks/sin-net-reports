package sinnet.gql.models;

import lombok.Data;

/** Fixme. */
@Data
public class CustomerSecretExGql {
  private String location;
  private String username;
  private String password;
  private String entityName;
  private String entityCode;
  private String changedWhen;
  private String changedWho;
}
