package sinnet.reports.report3;

import io.vavr.collection.Seq;

record ReportRequest(Seq<GroupDetails> activities) {
}
