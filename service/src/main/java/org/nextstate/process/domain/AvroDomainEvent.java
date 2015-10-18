package org.nextstate.process.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.EventSourceIdentifier;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class AvroDomainEvent<T> extends DomainEvent {
    private static DecoderFactory DECODER_FACTORY = new DecoderFactory();

    public AvroDomainEvent(EventSourceIdentifier id) {
        super(id);
    }

    protected byte[] getBytes(SpecificDatumWriter<T> writer, Schema schema, T event, boolean binary) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);

        if (binary) {
            try {
                writer.write(event, encoder);
                encoder.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteBuffer serialized = ByteBuffer.allocate(out.toByteArray().length);
            serialized.put(out.toByteArray());

            return serialized.array();

        } else {
            try {
                JsonEncoder jsonEncoder = EncoderFactory.get().jsonEncoder(schema, out);
                writer.write(event, jsonEncoder);
                jsonEncoder.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out.toByteArray();
        }
    }

    protected T readBytes(SpecificDatumReader<T> reader, Schema schema, byte[] byteArray, boolean binary) {
        InputStream is = new ByteArrayInputStream(byteArray);
        Decoder decoder = null;
        if (binary) {

            decoder = DECODER_FACTORY.binaryDecoder(is, null);
        } else {
            try {
                decoder = DECODER_FACTORY.jsonDecoder(schema, is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        T event = null;
        try {
            event = reader.read(null, decoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }
}
