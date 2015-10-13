package org.nextstate.eventsourcing.event;

import eventsourcing.event.Event;
import eventsourcing.event.EventSource;
import org.apache.avro.specific.SpecificRecordBase;

public abstract class DomainEventBase<T extends EventSource> extends SpecificRecordBase implements Event<T> {

    @Override
    public Class<T> getEventSourceType() {
        try {
            Class clazz = Class.forName(getEventSourceTypeName());
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    abstract public String getEventSourceTypeName();
}
