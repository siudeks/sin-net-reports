package sinnet.gql.models;

import lombok.Data;
import lombok.experimental.Accessors;

/** Fixme. */
@Data
@Accessors(chain = true)
public class SomeEntityGql {
  private String entityId;
  private Long entityVersion;
  private String projectId;
}
