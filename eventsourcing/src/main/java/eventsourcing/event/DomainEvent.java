package eventsourcing.event;

/*
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
*/
public abstract class DomainEvent<T extends EventSource> implements Event<T> {

    private EventSourceIdentifier eventSourceIdentifier;
    private Class<T> eventSourceType;
    private long sequenceNumber;

    public DomainEvent(Class<T> type, EventSourceIdentifier id) {
        this.eventSourceIdentifier = id;
        this.eventSourceType = type;
    }

    public DomainEvent() {
        // Jackson
    }

    public byte[] getAvroByteArray(boolean binary) {
        // dummy
        return null;
    }

    public void readAvroByteArray(byte[] byteArray, boolean binary) {
        // dummy
    }

    @Override
    public EventSourceIdentifier getEventSourceIdentifier() {
        return eventSourceIdentifier;
    }

    @Override
    public Class<T> getEventSourceType() {
        return eventSourceType;
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
                + ", eventSourceType=" + eventSourceType
                + ", sequenceNumber=" + sequenceNumber
                + '}';
    }
}
