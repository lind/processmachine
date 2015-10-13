package eventsourcing.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class DummyAggregateCreatedEvent extends DomainEvent<DummyAggregateRoot> {

    private final String name;

    public DummyAggregateCreatedEvent(EventSourceIdentifier id, String name) {
        super(DummyAggregateRoot.class, id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
