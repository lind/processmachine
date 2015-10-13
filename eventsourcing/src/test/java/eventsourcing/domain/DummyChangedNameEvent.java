package eventsourcing.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class DummyChangedNameEvent extends DomainEvent<DummyAggregateRoot> {

    private final String name;

    public DummyChangedNameEvent(EventSourceIdentifier id, String name) {
        super(DummyAggregateRoot.class, id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
