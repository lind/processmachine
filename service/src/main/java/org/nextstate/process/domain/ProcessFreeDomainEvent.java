package org.nextstate.process.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.UUIDEventSourceIdentifier;
import org.nextstate.process.event.ProcessFreeEvent;

public class ProcessFreeDomainEvent extends DomainEvent {
    private final ProcessFreeEvent processFreeEvent;

    public ProcessFreeDomainEvent(UUIDEventSourceIdentifier eventSourceIdentifier, ProcessFreeEvent event) {
        this.processFreeEvent = event;
    }

    public ProcessFreeEvent getProcessFreeEvent() {
        return processFreeEvent;
    }
}
