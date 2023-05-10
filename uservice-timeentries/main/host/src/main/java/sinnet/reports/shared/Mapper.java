package sinnet.reports.shared;

import java.time.LocalDate;
import java.time.YearMonth;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import sinnet.reports.grpc.Date;

/** TBD. */
@UtilityClass
public class Mapper {

  /** TBD. */
  public static Option<LocalDate> fromDto(Date x) {
    return Try.of(() -> LocalDate.of(x.getYear(), x.getMonth(), x.getDayOfTheMonth())).toOption();
  }

  /** TBD. */
  public static YearMonth fromDto(sinnet.reports.grpc.YearMonth x) {
    return YearMonth.of(x.getYear(), x.getMonth());
  }

}
