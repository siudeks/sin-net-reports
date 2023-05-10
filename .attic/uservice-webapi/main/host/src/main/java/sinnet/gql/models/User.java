package sinnet.gql.models;

import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.NonNull;

import lombok.Data;

@Data
public class User {
  private @NonNull @Id String entityId;
  private @NonNull String email;
}
