package org.nextstate.process.application;

import java.util.Optional;
import java.util.UUID;

import eventsourcing.event.UUIDEventSourceIdentifier;
import eventsourcing.eventstore.EventStore;
import org.nextstate.process.command.CreateProcessCommand;
import org.nextstate.process.command.LockProcessCommand;
import org.nextstate.process.domain.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessCommandHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EventStore eventStore;

    public ProcessCommandHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void handle(CreateProcessCommand command) {

        Process process = new Process(command.getId(), command.getProcessType(), command.getActivityType(),
                command.getActivityType());

        eventStore.save(process);
        process.clearUnsavedEvents(); // TODO nedded?
    }

    public void handle(LockProcessCommand command) {

        Optional<Process> process = eventStore
                .loadEventSource(Process.class, new UUIDEventSourceIdentifier(UUID.fromString(
                        command.getId())));
        process.orElseThrow(() -> new MissingProcessException(command.getId()));

        Process theProcess = process.get();
        theProcess.takeProcess(command.getIdent());
        eventStore.save(theProcess);
        theProcess.clearUnsavedEvents();
    }
}
