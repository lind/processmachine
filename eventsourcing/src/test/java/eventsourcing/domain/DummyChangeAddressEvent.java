package eventsourcing.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class DummyChangeAddressEvent extends DomainEvent<DummyAggregateRoot> {

    private final String address;

    public DummyChangeAddressEvent(EventSourceIdentifier id, String address) {
        super(DummyAggregateRoot.class, id);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "DummyChangeAddressEvent{" +
                "address='" + address + '\'' +
                '}';
    }
}
