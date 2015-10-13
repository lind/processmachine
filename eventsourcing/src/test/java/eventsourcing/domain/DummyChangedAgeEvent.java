package eventsourcing.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class DummyChangedAgeEvent extends DomainEvent<DummyAggregateRoot> {

    public final int age;

    public DummyChangedAgeEvent(EventSourceIdentifier id, int age) {
        super(DummyAggregateRoot.class, id);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "DummyChangedAgeEvent{" +
                "age=" + age +
                '}';
    }
}
