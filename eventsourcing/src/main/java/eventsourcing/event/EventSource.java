package eventsourcing.event;

import java.util.List;
import java.util.stream.Stream;

/**
 * Kilde til event, hendelser, i domenet. Hver event source har sin egen rekke med event som er lagret i event store.
 * Eventene brukes til å bygge opp tilstanden på event sourcen. I DDD er aggregater event sources.
 */
public interface EventSource<T extends EventSourceIdentifier> {

    void load(Stream<? extends Event> events);

    T getEventSourceIdentifier();

    List<DomainEvent> getUnsavedEvents();

    void clearUnsavedEvents();
}
