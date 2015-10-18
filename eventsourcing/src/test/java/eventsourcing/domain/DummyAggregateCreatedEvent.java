package eventsourcing.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class DummyAggregateCreatedEvent extends DomainEvent {

    private final String name;

    public DummyAggregateCreatedEvent(EventSourceIdentifier id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
