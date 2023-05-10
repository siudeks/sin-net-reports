package sinnet.reports.report2;

import java.time.YearMonth;

import sinnet.reports.shared.Kilometers;
import sinnet.reports.shared.Minutes;

record ActivityDetails(YearMonth period, String personName, Kilometers kilometers, Minutes minutes) {
}
