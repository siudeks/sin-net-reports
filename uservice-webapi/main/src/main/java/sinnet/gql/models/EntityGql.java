package sinnet.gql.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Fixme. */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class EntityGql {
  private String projectId;
  private String entityId;
  private long entityVersion;
}
