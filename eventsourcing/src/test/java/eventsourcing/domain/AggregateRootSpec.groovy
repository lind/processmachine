package eventsourcing.domain

import eventsourcing.event.DomainEvent
import eventsourcing.event.UUIDEventSourceIdentifier
import spock.lang.Specification

public class AggregateRootSpec extends Specification {

  public void shouldGetNewName() {
    given:
      UUIDEventSourceIdentifier id = new UUIDEventSourceIdentifier()
      DummyAggregateRoot dummyAggregate = new DummyAggregateRoot(id, "stefan")

    when:
      dummyAggregate.changeName("kalle")

    then:
      dummyAggregate.getName() == "kalle"
      dummyAggregate.getUnsavedEvents().size() == 2
  }

  public void shouldReloadStateFromEvents() {
    given:
      UUIDEventSourceIdentifier id = new UUIDEventSourceIdentifier()
      DummyAggregateRoot dummyAggregate = new DummyAggregateRoot(id, "arne")

      List<DomainEvent> events = new ArrayList<>()
      events.add(new DummyChangedNameEvent(id, "nisse"))
      events.add(new DummyChangedNameEvent(id, "anna"))

    when:
      dummyAggregate.load(events.stream())

    then:
      dummyAggregate.getName() == "anna"
  }
}
