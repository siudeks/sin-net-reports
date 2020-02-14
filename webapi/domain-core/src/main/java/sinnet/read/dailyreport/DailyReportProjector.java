package sinnet.read.dailyreport;

import org.springframework.data.domain.Example;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.Setter;
import sinnet.appevents.ServicesProjection;
import sinnet.events.NewServiceRegistered;
import sinnet.read.DailyReports;

/**
 * Manages projection of services provided for clients.
 */
@Service
public class DailyReportProjector {

    /** Projection storage. */
    @Autowired
    @Setter
    private DailyReportRepository repository;

    /** Used to inform about changes in projection. */
    @Autowired
    @Setter
    private ApplicationEventPublisher publisher;

    /**
     * Include just created service in list of services.
     * @param evt event
     * */
    @EventHandler
    public void on(final NewServiceRegistered evt) {
        var model = new DailyReportEntry();
        model.setWhen(evt.getWhen());
        repository.save(model);

        publisher.publishEvent(new ServicesProjection.Changed());
    }

    /**
     * Query handler.
     * @param ask query
     * @return an answer
     */
    @QueryHandler
    public DailyReports.Reply reply(final DailyReports.Ask ask) {
        var summary = new DailyReports.ServiceSummary();
        summary.setWhen(ask.getWhen());
        return DailyReports.Reply.Some.builder().entry(summary).build();
    }

    /**
     * Query handler. See {@link RegisteredServices}
     * @param ask see {@link RegisteredServices.Ask}
     * @return see {@link RegisteredServices.Reply}
     */
    @QueryHandler
    public RegisteredServices.Reply reply(final RegisteredServices.Ask ask) {

        var probe = new DailyReportEntry();
        probe.setWhen(ask.getFilterToDay());
        var example = Example.of(probe);

        var items = repository.findAll(example).stream()
                    .map(it -> new RegisteredServices.ServiceEntry())
                    .toArray(RegisteredServices.ServiceEntry[]::new);

        var result = new RegisteredServices.Reply();
        result.setEntries(items);
        return result;
    }
}
