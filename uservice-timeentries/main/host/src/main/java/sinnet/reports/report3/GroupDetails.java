package sinnet.reports.report3;

import io.vavr.collection.Seq;

record GroupDetails(String personName, Seq<CustomerDetails> details) {
}
