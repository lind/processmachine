package org.nextstate.process.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSource;
import eventsourcing.event.EventSourceIdentifier;
import org.apache.avro.io.DecoderFactory;

// is it possible to move any of the Avro logic in the domain events here?
public class AvroDomainEvent<T extends EventSource> extends DomainEvent<T> {

    protected static DecoderFactory DECODER_FACTORY = new DecoderFactory();

    public AvroDomainEvent(Class<T> clazz, EventSourceIdentifier id) {
        super(clazz, id);
    }
}
