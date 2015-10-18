package eventsourcing.eventstore;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import eventsourcing.event.EventSource;
import eventsourcing.event.EventSourceIdentifier;

public interface EventStore {

    <T extends EventSource> Optional<T> loadEventSource(Class<T> type, EventSourceIdentifier id);

    <T extends EventSource> void save(T eventsource);

    /**
     * Instantiates a event source with given identifier.
     *
     * @param type EventSource type
     * @param id   identifier
     * @param <T>  type of the event source
     * @return instance of the event source
     */
    default <T> T createEventSource(Class<T> type, EventSourceIdentifier id) {
        T createdAggregateRoot;
        try {
            Constructor<T> constructor;
            constructor = type.getConstructor(id.getClass());
            createdAggregateRoot = constructor.newInstance(id);

        } catch (SecurityException | IllegalAccessException e) {
            throw new IllegalStateException("Could not access constructor: " + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Missing constructor: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Constructor argument invalid: " + e.getMessage(), e);
        } catch (InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException("Construction failed: " + e.getMessage(), e);
        }
        return createdAggregateRoot;
    }

}
