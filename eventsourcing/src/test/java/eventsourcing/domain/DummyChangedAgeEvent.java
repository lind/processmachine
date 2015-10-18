package eventsourcing.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class DummyChangedAgeEvent extends DomainEvent {

    public final int age;

    public DummyChangedAgeEvent(EventSourceIdentifier id, int age) {
        super(id);
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
