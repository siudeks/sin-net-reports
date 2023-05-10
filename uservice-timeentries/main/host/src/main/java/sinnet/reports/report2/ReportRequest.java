package sinnet.reports.report2;

import io.vavr.collection.Seq;

record ReportRequest(
    Seq<ActivityDetails> activities) {
}
