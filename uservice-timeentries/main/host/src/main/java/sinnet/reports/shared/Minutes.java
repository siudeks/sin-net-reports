package sinnet.reports.shared;

import lombok.Value;

/** TBD. */
@Value(staticConstructor = "of")
public class Minutes {

  private int value;

  /** TBD. */
  public Minutes add(Minutes other) {
    return Minutes.of(value + other.value);
  }

  /** Default, company-wide approved formatting for time. */
  public String asString() {

    var minutesPerHour = 60;
    var minutes = value % minutesPerHour;
    var hours = value / minutesPerHour;

    var minumuValueWithDoubleDigits = 10;
    var minutesPrefix = minutes < minumuValueWithDoubleDigits ? ":0" : ":";
    var minutesAsText = minutesPrefix + minutes;

    return hours + minutesAsText;
  }
}
