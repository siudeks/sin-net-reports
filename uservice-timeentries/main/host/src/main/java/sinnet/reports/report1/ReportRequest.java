package sinnet.reports.report1;

import io.vavr.collection.Seq;

/**
 * Represents printable data content of a report. Useful for testing before
 * being converted to PDF file.
 */
record ReportRequest(
    CustomerDetails customer,
    Seq<ActivityDetails> activities) {
}

