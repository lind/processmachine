package org.nextstate.process.domain;

import eventsourcing.event.UUIDEventSourceIdentifier;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.nextstate.process.event.ProcessTakenEvent;

public class ProcessTakenDomainEvent extends AvroDomainEvent<ProcessTakenEvent> {
    private ProcessTakenEvent processLockedEvent;

    // Constructor when loading from eventstore
    public ProcessTakenDomainEvent(UUIDEventSourceIdentifier id) {
        super(id);
    }

    public ProcessTakenDomainEvent(UUIDEventSourceIdentifier id, ProcessTakenEvent event) {
        super(id);
        this.processLockedEvent = event;
    }

    public ProcessTakenEvent getProcessLockedEvent() {
        return processLockedEvent;
    }

    @Override public byte[] getEventByteArray(boolean binary) {
        SpecificDatumWriter<ProcessTakenEvent> writer = new SpecificDatumWriter<>(
                ProcessTakenEvent.class);

        return getBytes(writer, ProcessTakenEvent.SCHEMA$, processLockedEvent, binary);
    }

    @Override public void readEventByteArray(byte[] byteArray, boolean binary) {
        SpecificDatumReader<ProcessTakenEvent> reader = new SpecificDatumReader<>(
                ProcessTakenEvent.class);

        processLockedEvent = readBytes(reader, ProcessTakenEvent.SCHEMA$, byteArray, binary);
    }
}
