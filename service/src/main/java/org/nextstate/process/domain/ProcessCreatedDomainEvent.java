package org.nextstate.process.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import eventsourcing.event.EventSourceIdentifier;
import eventsourcing.event.UUIDEventSourceIdentifier;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.nextstate.process.event.ProcessCreatedEvent;

public class ProcessCreatedDomainEvent extends AvroDomainEvent<Process> {

    private static DecoderFactory DECODER_FACTORY = new DecoderFactory();

    private ProcessCreatedEvent processCreatedEvent;

    public ProcessCreatedDomainEvent(EventSourceIdentifier id) {
        super(Process.class, id);
    }

    public ProcessCreatedDomainEvent(UUIDEventSourceIdentifier id) {
        super(Process.class, id);
    }

    public ProcessCreatedDomainEvent(EventSourceIdentifier id, ProcessCreatedEvent event) {
        super(Process.class, id);
        this.processCreatedEvent = event;
    }

    public ProcessCreatedEvent getProcessCreatedEvent() {
        return processCreatedEvent;
    }

    @Override public byte[] getAvroByteArray(boolean binary) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out,
                null);

        SpecificDatumWriter<ProcessCreatedEvent> writer = new SpecificDatumWriter<>(
                ProcessCreatedEvent.class);

        if (binary) {

            try {
                writer.write(processCreatedEvent, encoder);
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
                        ProcessCreatedEvent.SCHEMA$, out);
                writer.write(processCreatedEvent, jsonEncoder);
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
                decoder = DECODER_FACTORY.jsonDecoder(ProcessCreatedEvent.SCHEMA$, is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SpecificDatumReader<ProcessCreatedEvent> reader = new SpecificDatumReader<>(
                ProcessCreatedEvent.class);

        try {
            processCreatedEvent = reader.read(null, decoder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
