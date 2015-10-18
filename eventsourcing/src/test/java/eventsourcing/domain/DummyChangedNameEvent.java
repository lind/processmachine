package eventsourcing.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class DummyChangedNameEvent extends DomainEvent {

    private final String name;

    public DummyChangedNameEvent(EventSourceIdentifier id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
