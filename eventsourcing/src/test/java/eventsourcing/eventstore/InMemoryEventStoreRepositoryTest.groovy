package eventsourcing.eventstore
import eventsourcing.domain.DummyAggregateRoot
import eventsourcing.domain.DummyChangedNameEvent
import eventsourcing.event.DomainEvent
import eventsourcing.event.EventSourceIdentifier
import eventsourcing.event.UUIDEventSourceIdentifier
import spock.lang.Specification

class InMemoryEventStoreRepositoryTest extends Specification {

  EventSourceIdentifier id_1 = new UUIDEventSourceIdentifier();
  EventSourceIdentifier id_2 = new UUIDEventSourceIdentifier();
  EventSourceIdentifier id_3 = new UUIDEventSourceIdentifier();

  def 'skal kunne lagre og hente ut events og aggregat'() {

    given:

      InMemoryEventStoreRepository repo = new InMemoryEventStoreRepository()
      List<DomainEvent> liste = opprettEventListeMedEttAggregat()

    when:
      repo.storeEventsAndUpdateAggregate(id_1.asString(), DummyAggregateRoot.class.getCanonicalName(), new Date(),
              liste)

    then:
      repo.getEventsByAggregateId(id_1.asString()).size() == 3
      repo.getAggregateVersion(id_1.asString()) == 3L

  }

  List<DomainEvent> opprettEventListeMedEttAggregat() {
    List<DomainEvent> eventListe = new ArrayList<>();

    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))
    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))
    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))

    return eventListe;
  }
}
