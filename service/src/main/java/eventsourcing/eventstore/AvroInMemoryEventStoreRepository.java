package eventsourcing.eventstore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;

public class AvroInMemoryEventStoreRepository implements EventStoreRepository {

    private final Map<String, List<AvroEvent>> avroEventStorage = new HashMap<>();
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

        List<DomainEvent> domainEvents = new ArrayList<>();

        if (avroEventStorage.containsKey(id)) {
            avroEventStorage.get(id).stream().forEach(e -> {

                DomainEvent domainEvent = (DomainEvent) createDomainEvent(e.getEventClazz(), e.getId());
                domainEvent.readEventByteArray(e.getEventByteArray(), false);
                domainEvents.add(domainEvent);
            });
        }

        return domainEvents;
    }

    @Override
    public Long getAggregateVersion(String id) {
        return aggregateStorage.get(id).getVersion();
    }

    @Override
    public void storeEventsAndUpdateAggregate(String id, String type, Date opprettetDato, List<DomainEvent> events) {

        List<AvroEvent> existingAvroEvents = avroEventStorage.getOrDefault(id, new ArrayList<>());

        events.stream().forEach(e -> {
            AvroEvent avroEvent = new AvroEvent(e.getClass(), e.getEventSourceIdentifier(), e.getEventByteArray(false));
            existingAvroEvents.add(avroEvent);
        });
        avroEventStorage.put(id, existingAvroEvents);

        Aggregate aggregate = aggregateStorage.getOrDefault(id, new Aggregate(id, type, 0L));
        aggregate.incrementVersion(events.size());
        aggregateStorage.put(id, aggregate);
    }

    /**
     * Instantiates a DomainEvent with given identifier.
     *
     * @param type DomainEvent type
     * @param id   identifier
     * @param <S>  type of the DomainEvent
     * @return instance of the DomainEvent
     */
    private <S> S createDomainEvent(Class<S> type, EventSourceIdentifier id) {
        if (null == id) {
            throw new IllegalArgumentException("The DomainEvent needs a id of the EventSource");
        }
        S createdDomainEvent;
        try {
            Constructor<S> constructor;
            constructor = type.getConstructor(id.getClass());
            createdDomainEvent = constructor.newInstance(id);

        } catch (SecurityException | IllegalAccessException e) {
            throw new IllegalStateException("Could not access constructor: " + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Missing constructor: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Constructor argument invalid: " + e.getMessage(), e);
        } catch (InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException("Construction failed: " + e.getMessage(), e);
        }
        return createdDomainEvent;
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

    private static class AvroEvent {
        private Class eventClazz;
        private byte[] eventByteArray;
        private EventSourceIdentifier id;

        public AvroEvent(Class clazz, EventSourceIdentifier id, byte[] eventByteArray) {
            this.eventClazz = clazz;
            this.id = id;
            this.eventByteArray = eventByteArray;
        }

        public Class getEventClazz() {
            return eventClazz;
        }

        public byte[] getEventByteArray() {
            return eventByteArray;
        }

        public EventSourceIdentifier getId() {
            return id;
        }
    }
}
