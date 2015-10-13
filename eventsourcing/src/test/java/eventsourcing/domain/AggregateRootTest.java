package eventsourcing.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import eventsourcing.event.DomainEvent;
import eventsourcing.event.UUIDEventSourceIdentifier;

public class AggregateRootTest {

    @Test
    public void shouldGetNewName() {
        // Given
        UUIDEventSourceIdentifier id = new UUIDEventSourceIdentifier();
        DummyAggregateRoot dummyAggregate = new DummyAggregateRoot(id, "stefan");

        // When
        dummyAggregate.changeName("kalle");

        // Then
        assertThat(dummyAggregate.getName(), is("kalle"));
        assertThat(dummyAggregate.getUnsavedEvents().size(), is(2));
    }

    @Test
    public void shouldReloadStateFromEvents() {
        // Given
        UUIDEventSourceIdentifier id = new UUIDEventSourceIdentifier();
        DummyAggregateRoot dummyAggregate = new DummyAggregateRoot(id, "arne");

        List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyChangedNameEvent(id, "nisse"));
        events.add(new DummyChangedNameEvent(id, "anna"));

        // When
        dummyAggregate.load(events.stream());

        // Then
        assertThat(dummyAggregate.getName(), is("anna"));
    }
}
