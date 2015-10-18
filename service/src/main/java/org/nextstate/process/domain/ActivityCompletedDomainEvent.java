package org.nextstate.process.domain;

import eventsourcing.event.DomainEvent;
import eventsourcing.event.UUIDEventSourceIdentifier;
import org.nextstate.process.event.ActivityCompletedEvent;

public class ActivityCompletedDomainEvent extends DomainEvent {
    private ActivityCompletedEvent activityCompletedEvent;
    private String result;

    // Constructor when loading from eventstore
    public ActivityCompletedDomainEvent(UUIDEventSourceIdentifier id) {
        super(id);
    }

    public ActivityCompletedDomainEvent(UUIDEventSourceIdentifier id, ActivityCompletedEvent event) {
        super(id);
        this.activityCompletedEvent = event;
    }

    public ActivityCompletedEvent getActivityCompletedEvent() {
        return activityCompletedEvent;
    }

    public String getResult() {
        return result;
    }
}
