package org.nextstate.process.domain;

import java.util.Optional;
import java.util.UUID;

import eventsourcing.domain.AnnotatedAggregateRoot;
import eventsourcing.event.EventHandler;
import eventsourcing.event.UUIDEventSourceIdentifier;
import org.nextstate.process.activity.Activity;
import org.nextstate.process.activity.ActivityState;
import org.nextstate.process.activity.TaskType;
import org.nextstate.process.event.ActivityCompletedEvent;
import org.nextstate.process.event.ActivityCreatedEvent;
import org.nextstate.process.event.ProcessCreatedEvent;
import org.nextstate.process.event.ProcessTakenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create process, activity
 * Take process - lock the process to a ident
 * Activity completed - the process is open, no "ready" activity (Created)
 * -- check the process state machine for new activities using the result as signal
 */
public class Process extends AnnotatedAggregateRoot<UUIDEventSourceIdentifier> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private String processState;
    private String processType;
    private String activityName; // TODO needed?
    private String activityType;
    private String taskType;

    protected Activity activity; // TODO keep string values or just Activity?

    //    private int sequenceNumber; // Version of the aggregate
    private long version = 0;

    private ProcessLockState processLockState;
    private String lockedBy;

    protected Optional<ProcessFlowResult> initFlow() {
        // Hook
        return Optional.empty();
    }

    protected Optional<ProcessFlowResult> flow(String processState, String result) {
        // Hook
        return Optional.empty();
    }

    // Constructor when loading from eventstore
    public Process(UUIDEventSourceIdentifier id) {
        super(id);
    }

    // TODO process name
    public Process(String id, String processType, String activityType, String taskType) {
        super(new UUIDEventSourceIdentifier(UUID.fromString(id)));

        // TODO validate Process type and Activity type and Activity task type

        Optional<ProcessFlowResult> flowResult = initFlow();

        String firstProcessState = null;
        if (flowResult.isPresent()) {
            firstProcessState = flowResult.get().getProcessState();
        }

        ProcessCreatedEvent event = ProcessCreatedEvent.newBuilder()
                .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                .setSequenceNumber(null) // SequenceNumber is nullable but in the Avro builder it is mandatory
                .setProcessType(processType)
                .setProcessState(firstProcessState)
                .build();

        apply(new ProcessCreatedDomainEvent(getEventSourceIdentifier(), event));

        if (flowResult.isPresent() && flowResult.get().getNextActivity() != null) {
            Activity nextActivity = flowResult.get().getNextActivity();

            ActivityCreatedEvent activityCreatedEvent = ActivityCreatedEvent.newBuilder()
                    .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                    .setActivity(nextActivity)
                    .setActivityType("todo?")
                    .setTaskType("taskType?")
                    .setSequenceNumber(null).build();

            apply(new ActivityCreatedDomainEvent(getEventSourceIdentifier(), activityCreatedEvent));
        }
    }

    public String getActivityName() {
        return activityName;
    }

    public ProcessLockState getProcessLockState() {
        return processLockState;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public String getProcessType() {
        return processType;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getTaskType() {
        return taskType;
    }

    public Activity getActivity() {
        return activity;
    }

    public String getProcessState() {
        return processState;
    }

    public long getVersion() {
        return version;
    }

    public void takeProcess(String identifier) {
        if (ProcessLockState.OPEN == processLockState && ActivityState.CREATED == activity.getState()) {
            ProcessTakenEvent event = ProcessTakenEvent.newBuilder()
                    .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                    .setLockedBy(identifier)
                    .setSequenceNumber(null).build();

            apply(new ProcessTakenDomainEvent(getEventSourceIdentifier(), event));
        } else {
            throw new ProcessLockedException(lockedBy);
        }
    }

    public void activityCompleted(String identifier, String result) {
        if (ProcessLockState.LOCKED == processLockState && ActivityState.IN_PROGRESS == activity.getState()) {
            if (lockedBy.equals(identifier)) {

                Optional<ProcessFlowResult> flowResult = flow(processState, result);

                String nextProcessState = null;
                if (flowResult.isPresent()) {
                    nextProcessState = flowResult.get().getProcessState();
                }

                ActivityCompletedEvent event = ActivityCompletedEvent.newBuilder()
                        .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                        .setIdentifier(identifier)
                        .setProcessState(nextProcessState)
                        .setSequenceNumber(null)
                        .setResult(result).build();

                apply(new ActivityCompletedDomainEvent(getEventSourceIdentifier(), event));

                if (flowResult.isPresent() && flowResult.get().getNextActivity() != null) {
                    Activity nextActivity = flowResult.get().getNextActivity();

                    if (nextActivity != null) {
                        ActivityCreatedEvent activityCreatedEvent = ActivityCreatedEvent.newBuilder()
                                .setEventSourceIdentifier(getEventSourceIdentifier().toString())
                                .setActivity(nextActivity)
                                .setActivityType("todo?")
                                .setTaskType("taskType?")
                                .setSequenceNumber(null).build();

                        apply(new ActivityCreatedDomainEvent(getEventSourceIdentifier(), activityCreatedEvent));
                    }
                } else {
                    throw new ProcessLockedException(lockedBy);
                }
            }
        }
    }

    @EventHandler
    public void handle(ProcessCreatedDomainEvent event) {
        processLockState = ProcessLockState.OPEN;
        processType = event.getProcessCreatedEvent().getProcessType();
        processState = event.getProcessCreatedEvent().getProcessState();
        version = event.getSequenceNumber();
    }

    @EventHandler
    public void handle(ActivityCreatedDomainEvent event) {
        activityType = event.getActivityCreatedEvent().getActivityType();
        TaskType taskType = TaskType.AUTOMATIC.toString().equals(event.getActivityCreatedEvent().getTaskType()) ?
                TaskType.AUTOMATIC :
                TaskType.MANUAL;
        version = event.getSequenceNumber();
        activity = event.getActivityCreatedEvent().getActivity();
    }

    @EventHandler
    public void handle(ProcessTakenDomainEvent event) {
        processLockState = ProcessLockState.LOCKED;
        lockedBy = event.getProcessLockedEvent().getLockedBy();
        version = event.getSequenceNumber();
        activity.setState(ActivityState.IN_PROGRESS);
    }

    @EventHandler
    public void handle(ActivityCompletedDomainEvent event) {
        processLockState = ProcessLockState.OPEN;
        version = event.getSequenceNumber();
        activity.setState(ActivityState.COMPLETED);
        lockedBy = null;
        processState = event.getActivityCompletedEvent().getProcessState();
    }
}
