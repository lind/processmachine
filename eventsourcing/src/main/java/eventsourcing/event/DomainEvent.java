package eventsourcing.event;

public abstract class DomainEvent implements Event {

    private EventSourceIdentifier eventSourceIdentifier;
    private long sequenceNumber;

    public DomainEvent(EventSourceIdentifier id) {
        this.eventSourceIdentifier = id;
    }

    public DomainEvent() {
        // Jackson
    }

    public byte[] getEventByteArray(boolean binary) {
        // hook
        return null;
    }

    public void readEventByteArray(byte[] byteArray, boolean binary) {
        // hook
    }

    @Override
    public EventSourceIdentifier getEventSourceIdentifier() {
        return eventSourceIdentifier;
    }

    @Override
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "DomainEvent{"
                + "eventSourceIdentifier=" + eventSourceIdentifier
                + ", sequenceNumber=" + sequenceNumber
                + '}';
    }
}
