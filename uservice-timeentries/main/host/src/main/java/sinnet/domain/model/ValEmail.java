package sinnet.domain.model;

import lombok.Value;
import lombok.experimental.Accessors;

/** Value type of email. */
@Value(staticConstructor = "of")
@Accessors(fluent = true)
public class ValEmail {
  private final String value;
}
