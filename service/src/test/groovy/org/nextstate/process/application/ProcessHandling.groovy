package org.nextstate.process.application

import eventsourcing.eventstore.AvroInMemoryEventStoreRepository
import eventsourcing.eventstore.EventStore
import eventsourcing.eventstore.EventStoreImpl
import eventsourcing.eventstore.InMemoryEventStoreRepository
import org.nextstate.process.command.CreateProcessCommand
import org.nextstate.process.command.LockProcessCommand
import spock.lang.Specification

class ProcessHandling extends Specification {


    def "create process with initial activity"() {
        given:
        def eventStore = Mock(EventStore)
        def handler = new ProcessCommandHandler(eventStore)
        when:
        def command = CreateProcessCommand.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setType("type")
                .setDescription("description")
                .setActivity("activity")
                .setActivityType("activityType").build()
        handler.handle(command)
        then:
        0 * eventStore.save()
    }


    def "save to store"() {
        given:
        def eventStoreRepository = new InMemoryEventStoreRepository()
        def eventStore = new EventStoreImpl(eventStoreRepository)
        def handler = new ProcessCommandHandler(eventStore)
        def id = UUID.randomUUID().toString()
        when:
        def command = CreateProcessCommand.newBuilder()
                .setId(id)
                .setType("type")
                .setDescription("description")
                .setActivity("activity")
                .setActivityType("activityType").build()
        handler.handle(command)
        then:
        eventStoreRepository.getEventsByAggregateId(id) != null
    }

    def "create and lock"() {
        given:
        def eventStoreRepository = new AvroInMemoryEventStoreRepository()
        def eventStore = new EventStoreImpl(eventStoreRepository)
        def handler = new ProcessCommandHandler(eventStore)
        def id = UUID.randomUUID().toString()
        when:
        def command = CreateProcessCommand.newBuilder()
                .setId(id)
                .setType("type")
                .setDescription("description")
                .setActivity("activity")
                .setActivityType("activityType").build()
        handler.handle(command)

        def lockProcessCommand = LockProcessCommand.newBuilder()
                .setId(id)
                .setIdent("kalle").build()
        handler.handle(lockProcessCommand)
        then:
        eventStoreRepository.getEventsByAggregateId(id) != null
        eventStoreRepository.getAggregateVersion(id) == 3
    }
}
