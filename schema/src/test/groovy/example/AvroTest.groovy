package example

import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.Encoder
import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.SpecificDatumReader
import org.apache.avro.specific.SpecificDatumWriter
import org.nextstate.process.activity.Activity
import org.nextstate.process.activity.ActivityState
import org.nextstate.process.activity.TaskType
import spock.lang.Specification

class AvroTest extends Specification {

    def "read Activity form JSON"() {
        given:
        def activity = Activity.newBuilder()
                .setId("id")
                .setDescription("description")
                .setState(ActivityState.CREATED)
                .setActivityType("activityType")
                .setTaskType(TaskType.AUTOMATIC).build()
        def activityJSON = "{\n" +
                "  \"id\" : \"id\",\n" +
                "  \"activityType\" : \"activityType\",\n" +
                "  \"taskType\" : \"AUTOMATIC\",\n" +
                "  \"description\" : {\n" +
                "    \"string\" : \"description\"\n" +
                "  },\n" +
                "  \"state\" : \"CREATED\"\n" +
                "}"

        when:
        def InputStream inputStream = new ByteArrayInputStream(activityJSON.getBytes())
        def decoder = DecoderFactory.get().jsonDecoder(Activity.SCHEMA$, inputStream)

        SpecificDatumReader<Activity> reader = new SpecificDatumReader<>(Activity.class);

        Activity activityRead = reader.read(null, decoder)

        then:
        activityRead.getId() == "id"
        activityRead.getDescription() == "description"
    }

    def "stream Activity to Json"() {
        given:
        def activity = Activity.newBuilder()
                .setId("id")
                .setDescription("description")
                .setState(ActivityState.CREATED)
                .setActivityType("activityType")
                .setTaskType(TaskType.AUTOMATIC).build()
        println "Activity:" + activity

        when:
        def outputStream = new ByteArrayOutputStream();
        def writer = new SpecificDatumWriter<>(Activity.class);

        //        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        Encoder encoder = EncoderFactory.get().jsonEncoder(Activity.SCHEMA$, outputStream, true);
        writer.write(activity, encoder);
        encoder.flush();
        outputStream.close();

        println "outputStream: \n " + outputStream.toString() + "\n"

        then:
        outputStream.toString().contains("\"state\" : \"CREATED\"");
    }

}
