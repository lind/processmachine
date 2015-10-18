package org.nextstate.process.domain;

import eventsourcing.event.UUIDEventSourceIdentifier;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.nextstate.process.event.ActivityCreatedEvent;

public class ActivityCreatedDomainEvent extends AvroDomainEvent<ActivityCreatedEvent> {
    private ActivityCreatedEvent activityCreatedEvent;

    // Constructor when loading from eventstore
    public ActivityCreatedDomainEvent(UUIDEventSourceIdentifier id) {
        super(id);
    }

    public ActivityCreatedDomainEvent(UUIDEventSourceIdentifier id, ActivityCreatedEvent event) {
        super(id);
        this.activityCreatedEvent = event;
    }

    public ActivityCreatedEvent getActivityCreatedEvent() {
        return activityCreatedEvent;
    }

    @Override public byte[] getEventByteArray(boolean binary) {

        SpecificDatumWriter<ActivityCreatedEvent> writer = new SpecificDatumWriter<>(
                ActivityCreatedEvent.class);

        return getBytes(writer, ActivityCreatedEvent.SCHEMA$, activityCreatedEvent, binary);
    }

    @Override public void readEventByteArray(byte[] byteArray, boolean binary) {

        SpecificDatumReader<ActivityCreatedEvent> reader = new SpecificDatumReader<>(
                ActivityCreatedEvent.class);

        activityCreatedEvent = readBytes(reader, ActivityCreatedEvent.SCHEMA$, byteArray, binary);
    }
}
