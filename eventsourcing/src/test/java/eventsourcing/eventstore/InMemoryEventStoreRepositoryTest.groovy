package eventsourcing.eventstore

import eventsourcing.domain.DummyAggregateRoot
import eventsourcing.domain.DummyChangedNameEvent
import eventsourcing.domain.YetAnotherDummyAggregateRoot
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

  def 'skal kunne lagre eventstream med tilhørende aggregater og hente ut disse ut igjen'() {

    given:

      InMemoryEventStoreRepository repo = new InMemoryEventStoreRepository()
      List<DomainEvent> liste = opprettEventListeMedFlereAggregat()

    when:
      repo.storeEventStreamAndUpdateAggregates(liste.stream())

    then:
      repo.getEventsByAggregateId(id_1.asString()).size() == 3
      repo.getEventsByAggregateId(id_2.asString()).size() == 1
      repo.getAggregateVersion(id_1.asString()) == 3L
      repo.getAggregateVersion(id_2.asString()) == 1L
  }

  List<DomainEvent> opprettEventListeMedFlereAggregat() {
    List<DomainEvent> eventListe = new ArrayList<>();

    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))
    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))
    eventListe.add(new DummyChangedNameEvent(id_2, 'dummy'))
    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))

    return eventListe;
  }

  List<DomainEvent> opprettEventListeMedEttAggregat() {
    List<DomainEvent> eventListe = new ArrayList<>();

    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))
    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))
    eventListe.add(new DummyChangedNameEvent(id_1, 'dummy'))

    return eventListe;
  }

  def 'skal kunne hente ut alle aggregat for type'() {
    given:
      InMemoryEventStoreRepository repo = new InMemoryEventStoreRepository()
      List<DomainEvent> liste = opprettEventListeMedFlereAggregat()
      repo.storeEventStreamAndUpdateAggregates(liste.stream())
      repo.storeEventsAndUpdateAggregate(id_3.asString(), YetAnotherDummyAggregateRoot.class.getCanonicalName(), new Date(),
              liste)

    when:
      List<String> dummyAggregates = repo.getAllAggregates(DummyAggregateRoot.class.getCanonicalName());
      List<String> otherDummyAggregates = repo.getAllAggregates(YetAnotherDummyAggregateRoot.class.getCanonicalName());


    then:
      dummyAggregates.size() == 2
      dummyAggregates.containsAll([ id_1.asString(), id_2.asString()])

      otherDummyAggregates.size() == 1
      otherDummyAggregates.get(0) == id_3.asString()
  }

}
