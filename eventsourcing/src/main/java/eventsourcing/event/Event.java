package eventsourcing.event;

public interface Event<T extends EventSource> {

    EventSourceIdentifier getEventSourceIdentifier();

    Class<T> getEventSourceType();

    long getSequenceNumber();

    void setSequenceNumber(long sequenceNumber);
}
