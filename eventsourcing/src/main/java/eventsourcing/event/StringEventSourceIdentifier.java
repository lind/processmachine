package eventsourcing.event;

public class StringEventSourceIdentifier implements EventSourceIdentifier<String> {

    private String identifier;

    public StringEventSourceIdentifier() {
        // jackson
    }

    public StringEventSourceIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String asString() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StringEventSourceIdentifier that = (StringEventSourceIdentifier) o;

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
        return "StringEventSourceIdentifier{" + "identifier='" + identifier + '\'' + '}';
    }
}
