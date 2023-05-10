package sinnet.models;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TBD.
 */
@Data
@Accessors(chain = true)
public final class CustomerModel {
  private ShardedId id;
  private CustomerValue value;
  private List<CustomerSecret> secrets = List.of();
  private List<CustomerSecretEx> secretsEx = List.of();
  private List<CustomerContact> contacts = List.of();
}
