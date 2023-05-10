package sinnet.reports.report1;

import java.time.LocalDate;

import io.vavr.control.Option;
import sinnet.reports.shared.Kilometers;
import sinnet.reports.shared.Minutes;

record SpecialActivityDetails(
    String description,
    String who,
    Option<LocalDate> when,
    Minutes howLongInMins,
    Kilometers howFarInKms) {
}
