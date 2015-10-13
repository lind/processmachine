package org.nextstate.process.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.UUIDEventSourceIdentifier;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.nextstate.process.event.ActivityCreatedEvent;

public class ActivityCreatedDomainEvent extends DomainEvent {
    private static DecoderFactory DECODER_FACTORY = new DecoderFactory();

    private ActivityCreatedEvent activityCreatedEvent;

    public ActivityCreatedDomainEvent(UUIDEventSourceIdentifier id) {
        super(Process.class, id);
    }

    public ActivityCreatedDomainEvent(UUIDEventSourceIdentifier id, ActivityCreatedEvent event) {
        super(Process.class, id);

        this.activityCreatedEvent = event;
    }

    public ActivityCreatedEvent getActivityCreatedEvent() {
        return activityCreatedEvent;
    }

    @Override public byte[] getAvroByteArray(boolean binary) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out,
                null);
        SpecificDatumWriter<ActivityCreatedEvent> writer = new SpecificDatumWriter<>(
                ActivityCreatedEvent.class);

        if (binary) {

            try {
                writer.write(activityCreatedEvent, encoder);
                encoder.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteBuffer serialized = ByteBuffer
                    .allocate(out.toByteArray().length);
            serialized.put(out.toByteArray());

            return serialized.array();

        } else {
            try {
                JsonEncoder jsonEncoder = EncoderFactory.get().jsonEncoder(
                        ActivityCreatedEvent.SCHEMA$, out);
                writer.write(activityCreatedEvent, jsonEncoder);
                jsonEncoder.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out.toByteArray();
        }

    }

    @Override public void readAvroByteArray(byte[] byteArray, boolean binary) {

        InputStream is = new ByteArrayInputStream(byteArray);
        Decoder decoder = null;
        if (binary) {

            decoder = DECODER_FACTORY.binaryDecoder(is, null);
        } else {
            try {
                decoder = DECODER_FACTORY.jsonDecoder(ActivityCreatedEvent.SCHEMA$, is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SpecificDatumReader<ActivityCreatedEvent> reader = new SpecificDatumReader<>(
                ActivityCreatedEvent.class);

        try {
            activityCreatedEvent = reader.read(null, decoder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
