package eventsourcing.eventstore;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eventsourcing.event.DomainEvent;

public class InMemoryEventStoreRepository implements EventStoreRepository {

    private final Map<String, List<DomainEvent>> eventStorage = new HashMap<>();
    private final Map<String, Aggregate> aggregateStorage = new HashMap<>();

    @Override
    public List<String> getAllAggregates(String type) {
        return aggregateStorage.values().stream()
                .filter(a -> a.getType().equalsIgnoreCase(type))
                .map(a -> a.getIdentifier())
                .collect(Collectors.toList());
    }

    @Override
    public List<DomainEvent> getEventsByAggregateId(String id) {
        return eventStorage.get(id);
    }

    @Override
    public Long getAggregateVersion(String id) {
        return aggregateStorage.get(id).getVersion();
    }

    @Override
    public void storeEventsAndUpdateAggregate(String id, String type, Date opprettetDato, List<DomainEvent> events) {
        List<DomainEvent> existingEvents = eventStorage.getOrDefault(id, new ArrayList<>());
        existingEvents.addAll(events);
        eventStorage.put(id, existingEvents);

        Aggregate aggregate = aggregateStorage.getOrDefault(id, new Aggregate(id, type, 0L));
        aggregate.incrementVersion(events.size());
        aggregateStorage.put(id, aggregate);
    }

    @Override
    @SuppressWarnings({ "checkstyle:indentation" })
    public void storeEventStreamAndUpdateAggregates(Stream<DomainEvent> stream) {

        stream.forEach(e -> {
            String id = e.getEventSourceIdentifier().asString();

            List<DomainEvent> events = eventStorage.getOrDefault(id,
                    new ArrayList<>());
            events.add(e);
            eventStorage.put(id, events);

            Aggregate aggregate = aggregateStorage.getOrDefault(id, new Aggregate(id, e.getEventSourceType()
                    .getCanonicalName(), 0L));
            aggregate.incrementVersion(1);
            aggregateStorage.put(id, aggregate);

        });
    }

    private static class Aggregate {

        private String identifier;

        private long version;

        private String type;

        public Aggregate(String identifier, String type, long version) {
            this.identifier = identifier;
            this.version = version;
            this.type = type;
        }

        public String getIdentifier() {
            return identifier;
        }

        public long getVersion() {
            return version;
        }

        public String getType() {
            return type;
        }

        public void incrementVersion(long with) {
            version += with;
        }

    }


}
