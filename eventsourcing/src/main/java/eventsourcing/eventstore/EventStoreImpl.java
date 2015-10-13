package eventsourcing.eventstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eventsourcing.event.DomainEvent;
import eventsourcing.event.Event;
import eventsourcing.event.EventSource;
import eventsourcing.event.EventSourceIdentifier;

@SuppressWarnings({ "checkstyle:indentation" })
public class EventStoreImpl implements EventStore {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private EventStoreRepository eventStoreRepository;

    public EventStoreImpl(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public <T extends EventSource> Optional<T> loadEventSource(Class<T> type, EventSourceIdentifier id) {
        logger.debug("loadEventSource - type: {}, identifier: {}", type, id);
        List<? extends Event> events = getEventsFromStore(id);
        if (null == events || events.isEmpty()) {
            return Optional.empty();
        }

        T eventSource = createEventSource(type, id);
        eventSource.load(events.stream());
        return Optional.of(eventSource);
    }

    private List<? extends Event> getEventsFromStore(EventSourceIdentifier id) {

        List<? extends Event> events = eventStoreRepository.getEventsByAggregateId(id.asString());

        logger.debug("Nr of events from source with id:{} are:{}", id.asString(),
                null == events ? 0 : events.size());

        return events;
    }

    @Override
    public <T extends EventSource> void save(T eventsource) {
        logger.debug("save() - eventsource: {}", eventsource);

        String key = eventsource.getEventSourceIdentifier().asString();

        List<DomainEvent> existingEvents = eventStoreRepository.getEventsByAggregateId(key);

        if (null == existingEvents) {
            existingEvents = new ArrayList<>();
        }

        int numberOfStoredEvents = existingEvents.size();
        List<DomainEvent> eventsToSave = eventsource.getUnsavedEvents();
        logger.debug("save() - number of events on {} before save: {}", eventsource.getEventSourceIdentifier()
                .asString(), numberOfStoredEvents);
        for (DomainEvent event : eventsToSave) {
            event.setSequenceNumber(++numberOfStoredEvents);
        }

        logger.debug("save() - Storing {} events to:{}", eventsToSave.size(), key);
        eventStoreRepository.storeEventsAndUpdateAggregate(key, eventsource.getClass().getCanonicalName(),
                new Date(), eventsToSave);
    }

    @Override
    public void storeEventStream(Stream<DomainEvent> stream) {
        eventStoreRepository.storeEventStreamAndUpdateAggregates(stream);
    }

}
