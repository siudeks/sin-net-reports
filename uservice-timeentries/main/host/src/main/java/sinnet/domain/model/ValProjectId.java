package sinnet.domain.model;

import java.util.UUID;

import lombok.Value;
import lombok.experimental.Accessors;

/** Value type of project id. */
@Value(staticConstructor = "of")
@Accessors(fluent = true)
public class ValProjectId {
  private final UUID value;
}
