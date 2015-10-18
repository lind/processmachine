package org.nextstate.process.application
import eventsourcing.eventstore.AvroInMemoryEventStoreRepository
import eventsourcing.eventstore.EventStoreImpl
import org.nextstate.process.command.CreateProcessCommand
import org.nextstate.process.command.LockProcessCommand
import spock.lang.Ignore
import spock.lang.Specification

class SimpleProcessSpec extends Specification {

    def eventStoreRepository
    def eventStore
    def handler

    def setup() {
        eventStoreRepository = new AvroInMemoryEventStoreRepository()
        eventStore = new EventStoreImpl(eventStoreRepository)
        handler = new ProcessCommandHandler(eventStore)

    }

    @Ignore // activity not created by default anymore
    def "create process and execute action"() {

        given:
        def id = UUID.randomUUID().toString()
        def createProcessCommand = CreateProcessCommand.newBuilder()
                .setId(id)
                .setType("type")
                .setDescription("description")
                .setActivity("activity")
                .setActivityType("activityType").build()
        def lockProcessCommand = LockProcessCommand.newBuilder()
                .setId(id)
                .setIdent("kalle").build()

        when:
        handler.handle(createProcessCommand)
        handler.handle(lockProcessCommand)

        then:
        eventStoreRepository.getEventsByAggregateId(id) != null
        eventStoreRepository.getAggregateVersion(id) == 3

    }
}
