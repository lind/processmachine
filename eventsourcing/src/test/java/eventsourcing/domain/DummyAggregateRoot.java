package eventsourcing.domain;

import eventsourcing.event.EventHandler;
import eventsourcing.event.UUIDEventSourceIdentifier;

public class DummyAggregateRoot extends AnnotatedAggregateRoot<UUIDEventSourceIdentifier> {

    private String address;
    private int age;
    private String name;

    public DummyAggregateRoot(UUIDEventSourceIdentifier id) {
        super(id);
    }

    public DummyAggregateRoot(UUIDEventSourceIdentifier id, String name) {
        this(id);
        apply(new DummyAggregateCreatedEvent(id, name));
    }

    public void changeAddress(String address) {
        apply(new DummyChangeAddressEvent(getEventSourceIdentifier(), address));
    }

    public void changeAge(int age) {
        apply(new DummyChangedAgeEvent(getEventSourceIdentifier(), age));
    }

    public void changeName(String name) {
        apply(new DummyChangedNameEvent(getEventSourceIdentifier(), name));
    }

    // =========================
    // Event handlers
    // =========================

    @EventHandler
    public void createEvent(DummyAggregateCreatedEvent event) {
        this.name = event.getName();
    }

    @EventHandler
    public void handleChangeAddressEvent(DummyChangeAddressEvent event) {
        this.address = event.getAddress();
    }

    @EventHandler
    public void handleChangedAge(DummyChangedAgeEvent event) {
        this.age = event.getAge();
    }

    @EventHandler
    public void changedNameEvent(DummyChangedNameEvent event) {
        this.name = event.getName();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "DummyAggregateRoot{"
                + "address='" + address + '\''
                + ", age=" + age
                + ", name='" + name + '\''
                + "} " + super.toString();
    }
}
