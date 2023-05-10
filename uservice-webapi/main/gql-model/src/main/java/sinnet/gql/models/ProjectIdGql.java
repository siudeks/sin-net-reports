package sinnet.gql.models;

import lombok.Data;
import lombok.experimental.Accessors;

/** Fixme. */
@Data
@Accessors(chain = true)
public class ProjectIdGql {
  private String id;
  private long tag;
}
