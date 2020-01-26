package sinnet.read.dailyreport;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import sinnet.DailyReport;

import sinnet.events.ServiceRegistered;

/**
 * Manages projection of services provided for clients.
 */
@Service
public class DailyReportProjector {

    /**
     * Include just created service in list of services.
     * @param evt event
     * */
    @EventHandler
    public void on(final ServiceRegistered evt) {
    }

    /**
     * Query handler.
     * @param ask query
     * @return an answer
     */
    @QueryHandler
    public DailyReport.Reply reply(final DailyReport.Ask ask) {
        var summary = new DailyReport.ServiceSummary();
        summary.setWhen(ask.getWhen());
        return DailyReport.Reply.Some.builder().entry(summary).build();
    }
}
