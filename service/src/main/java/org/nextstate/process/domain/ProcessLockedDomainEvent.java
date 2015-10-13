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
import org.nextstate.process.event.ProcessLockedEvent;

public class ProcessLockedDomainEvent extends DomainEvent {
    private static DecoderFactory DECODER_FACTORY = new DecoderFactory();

    private ProcessLockedEvent processLockedEvent;

    public ProcessLockedDomainEvent(UUIDEventSourceIdentifier id) {
        super(Process.class, id);
    }

    public ProcessLockedDomainEvent(UUIDEventSourceIdentifier id, ProcessLockedEvent event) {
        super(Process.class, id);
        this.processLockedEvent = event;
    }

    public ProcessLockedEvent getProcessLockedEvent() {
        return processLockedEvent;
    }

    @Override public byte[] getAvroByteArray(boolean binary) {
        SpecificDatumWriter<ProcessLockedEvent> writer = new SpecificDatumWriter<>(
                ProcessLockedEvent.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out,
                null);

        if (binary) {
            try {
                writer.write(processLockedEvent, encoder);
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
                        ProcessLockedEvent.SCHEMA$, out);
                writer.write(processLockedEvent, jsonEncoder);
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
                decoder = DECODER_FACTORY.jsonDecoder(ProcessLockedEvent.SCHEMA$, is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SpecificDatumReader<ProcessLockedEvent> reader = new SpecificDatumReader<>(
                ProcessLockedEvent.class);

        try {
            processLockedEvent = reader.read(null, decoder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
