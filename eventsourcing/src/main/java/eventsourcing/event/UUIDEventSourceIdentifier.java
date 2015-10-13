package eventsourcing.event;

import java.util.UUID;

public class UUIDEventSourceIdentifier implements EventSourceIdentifier<UUID> {
    private final UUID identifier;

    public UUIDEventSourceIdentifier() {
        this.identifier = UUID.randomUUID();
    }

    public UUIDEventSourceIdentifier(UUID id) {
        this.identifier = id;
    }

    @Override
    public UUID getIdentifier() {
        return identifier;
    }

    @Override
    public String asString() {
        return identifier.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UUIDEventSourceIdentifier that = (UUIDEventSourceIdentifier) o;

        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UUIDEventSourceIdentifier{" + "identifier=" + identifier + '}';
    }
}
