package sinnet.reports.shared;

import lombok.Value;

/** TBD. */
@Value(staticConstructor = "of")
public class Kilometers {
  int value;

  /** TBD. */
  public Kilometers add(Kilometers other) {
    return Kilometers.of(value + other.value);
  }

  /** TBD. */
  @Override
  public String toString() {
    return Integer.toString(value);
  }
}
