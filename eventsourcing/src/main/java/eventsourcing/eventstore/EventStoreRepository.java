package eventsourcing.eventstore;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import eventsourcing.event.DomainEvent;

public interface EventStoreRepository {

    List<String> getAllAggregates(String type);

    List<DomainEvent> getEventsByAggregateId(String id);

    Long getAggregateVersion(String id);

    void storeEventsAndUpdateAggregate(String id, String type, Date opprettetDato, List<DomainEvent> event);

    void storeEventStreamAndUpdateAggregates(Stream<DomainEvent> eventStream);

}
