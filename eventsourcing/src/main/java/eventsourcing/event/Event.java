package eventsourcing.event;

public interface Event {

    EventSourceIdentifier getEventSourceIdentifier();

    long getSequenceNumber();

    void setSequenceNumber(long sequenceNumber);
}
