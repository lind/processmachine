package org.nextstate.process.domain;

import eventsourcing.event.EventSourceIdentifier;
import eventsourcing.event.UUIDEventSourceIdentifier;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.nextstate.process.event.ProcessCreatedEvent;

public class ProcessCreatedDomainEvent extends AvroDomainEvent<ProcessCreatedEvent> {
    private ProcessCreatedEvent processCreatedEvent;

    // Constructor when loading from eventstore
    public ProcessCreatedDomainEvent(UUIDEventSourceIdentifier id) {
        super(id);
    }

    public ProcessCreatedDomainEvent(EventSourceIdentifier id, ProcessCreatedEvent event) {
        super(id);
        this.processCreatedEvent = event;
    }

    public ProcessCreatedEvent getProcessCreatedEvent() {
        return processCreatedEvent;
    }

    @Override public byte[] getEventByteArray(boolean binary) {

        SpecificDatumWriter<ProcessCreatedEvent> writer = new SpecificDatumWriter<>(
                ProcessCreatedEvent.class);

        return getBytes(writer, ProcessCreatedEvent.SCHEMA$, processCreatedEvent, binary);
    }

    @Override public void readEventByteArray(byte[] byteArray, boolean binary) {

        SpecificDatumReader<ProcessCreatedEvent> reader = new SpecificDatumReader<>(
                ProcessCreatedEvent.class);

        processCreatedEvent = readBytes(reader, ProcessCreatedEvent.SCHEMA$, byteArray, binary);
    }
}
