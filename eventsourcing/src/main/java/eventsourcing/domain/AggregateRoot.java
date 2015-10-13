package eventsourcing.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eventsourcing.event.DomainEvent;
import eventsourcing.event.Event;
import eventsourcing.event.EventSource;
import eventsourcing.event.EventSourceIdentifier;

/**
 * Aggregat som er bygget opp av hendelser. Endringer i aggregatet resulterer i hendelser som lagres og brukes for å
 * laste inn tilstandet til aggregatet.
 */
public abstract class AggregateRoot<T extends EventSourceIdentifier> implements EventSource<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final T id;
    private final List<DomainEvent> unsavedEvents = new ArrayList<>();

    public AggregateRoot(T id) {
        this.id = id;
    }

    protected void apply(DomainEvent event) {
        logger.debug("apply() - event: {} - to Aggregate: {} ", event, this.getClass().getSimpleName());
        unsavedEvents.add(event);
        handle(event);
    }

    @Override
    public T getEventSourceIdentifier() {
        return id;
    }

    @Override
    public void load(Stream<? extends Event> events) {
        events.forEach(this::handle);
    }

    @Override
    public List<DomainEvent> getUnsavedEvents() {
        return new ArrayList<>(unsavedEvents);
    }

    @Override
    public void clearUnsavedEvents() {
        unsavedEvents.clear();
    }

    protected abstract void handle(Event event);

}
