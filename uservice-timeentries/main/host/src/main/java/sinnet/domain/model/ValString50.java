package sinnet.domain.model;

import javax.annotation.Nullable;

import lombok.EqualsAndHashCode;

/** Holds text no longer than 50 chars. */
@EqualsAndHashCode
public final class ValString50 {

  private static final ValString50 EMPTY = ValString50.of(null);

  public static ValString50 empty() {
    return EMPTY;
  }

  private final String value;
  
  public String getValue() {
    return value;
  }

  private ValString50(String value) {
    this.value = value;
  }

  /** Creates new instance of ValString50. */
  public static ValString50 of(@Nullable String value) {
    return value == null
      ? empty()
      : new ValString50(value);
  }
}
