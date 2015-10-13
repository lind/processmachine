package org.nextstate.process.domain;

import java.util.UUID;

import eventsourcing.domain.AnnotatedAggregateRoot;
import eventsourcing.event.EventHandler;
import eventsourcing.event.UUIDEventSourceIdentifier;
import org.nextstate.process.event.ActivityCreatedEvent;
import org.nextstate.process.event.ProcessCreatedEvent;
import org.nextstate.process.event.ProcessLockedEvent;
import org.nextstate.process.event.ProcessFreeEvent;

public class Process extends AnnotatedAggregateRoot<UUIDEventSourceIdentifier> {
    private String activity;
    private String activityType;
//    private int sequenceNumber;
    private ProcessLockState processLockState;
    private String lockedBy;

    // Constructor when loading from eventstore
    public Process(UUIDEventSourceIdentifier id) {
        super(id);
    }

    public Process(String id, String activity, String activityType) {
        super(new UUIDEventSourceIdentifier(UUID.fromString(id)));
        ProcessCreatedEvent event = ProcessCreatedEvent.newBuilder()
                .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                .setActivity(activity)
                .setSequenceNumber(null).build(); // SequenceNumber is nullable but in the Avro builder it is mandatory

        apply(new ProcessCreatedDomainEvent(getEventSourceIdentifier(), event));

        ActivityCreatedEvent activityCreatedEvent = ActivityCreatedEvent.newBuilder()
                .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                .setActivity(activity)
                .setActivityType(activityType)
                .setSequenceNumber(null).build();

        apply(new ActivityCreatedDomainEvent(getEventSourceIdentifier(), activityCreatedEvent));
    }

    public String getActivity() {
        return activity;
    }

    public ProcessLockState getProcessLockState() {
        return processLockState;
    }

    public String getLockedBy() {
        return lockedBy;
    }

/*    public int getSequenceNumber() {
        return sequenceNumber;
    }
*/
    public void takeProcess(String identifier) {
        if (processLockState != ProcessLockState.LOCKED) {
            ProcessLockedEvent event = ProcessLockedEvent.newBuilder()
                    .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                    // TODO is getEventSourceType rely needed in the DomainEvent?
                    .setLockedBy(identifier)
                    .setSequenceNumber(null).build();

            apply(new ProcessLockedDomainEvent(getEventSourceIdentifier(), event));
        } else {
            throw new ProcessLockedException(lockedBy);
        }
    }

    public void freeProcess(String identifier) {
        if (processLockState == ProcessLockState.LOCKED) {
            if (lockedBy.equals(identifier)) {
                ProcessFreeEvent event = ProcessFreeEvent.newBuilder()
                        .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                        .setFreedBy(identifier)
                        .setSequenceNumber(null).build();

                apply(new ProcessFreeDomainEvent(getEventSourceIdentifier(), event));
            } else {
                throw new ProcessLockedException(lockedBy);
            }
        }
    }

    @EventHandler
    public void handle(ProcessCreatedDomainEvent event) {
        // TODO activity here?
        activity = event.getProcessCreatedEvent().getActivity();
//        sequenceNumber = event.getProcessCreatedEvent().getSequenceNumber();
    }

    @EventHandler
    public void handle(ActivityCreatedDomainEvent event) {
        activity = event.getActivityCreatedEvent().getActivity();
        activityType = event.getActivityCreatedEvent().getActivityType();
    }

    @EventHandler
    public void handle(ProcessLockedDomainEvent event) {
        processLockState = ProcessLockState.LOCKED;
        lockedBy = event.getProcessLockedEvent().getLockedBy();
    }

    @EventHandler
    public void handle(ProcessFreeDomainEvent event) {
        processLockState = ProcessLockState.OPEN;
        lockedBy = null;
    }


}
