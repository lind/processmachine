package org.nextstate.process.domain;

import static org.nextstate.statemachine.SimpleState.state;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import eventsourcing.event.UUIDEventSourceIdentifier;
import org.nextstate.process.activity.Activity;
import org.nextstate.process.activity.ActivityState;
import org.nextstate.process.activity.TaskType;
import org.nextstate.statemachine.Action;
import org.nextstate.statemachine.FinalState;
import org.nextstate.statemachine.State;
import org.nextstate.statemachine.StateMachine;

public class ProcessWithASimpleFlow extends Process {

    private ASimpleStateMachine flow;
    private Activity nextActivity = null;

    public static final String A_SIMPLE_STATE = "ASimpleState";
    public static final String A_SIMPLE_STATE_2 = "ASimpleState2";
    private static final String FINAL = "Final";
    private static final String FINAL_STATE = "FinalState";
    public static final String A_SIMPLE_EVENT = "ASimpleEvent";
    private static final String A_SIMPLE_ACTION = "ASimpleAction";

    // Constructor when loading from eventstore
    public ProcessWithASimpleFlow(UUIDEventSourceIdentifier id) {
        super(id);
    }

    public ProcessWithASimpleFlow(String id, String processType, String activityType, String taskType) {
        super(id, processType, activityType, taskType);
    }

    @Override protected Optional<ProcessFlowResult> initFlow() {
        ASimpleStateMachine stateMachine = new ASimpleStateMachine();
        String initState = stateMachine.getActiveStateName();

        System.out.println("Init state: " + initState);
        log.debug("Init state: {}", initState);
        return Optional.of(new ProcessFlowResult(initState, nextActivity));
    }

    @Override protected Optional<ProcessFlowResult> flow(String processState, String result) {
        ASimpleStateMachine stateMachine = new ASimpleStateMachine();
        stateMachine.activeStateConfiguration(processState);
        stateMachine.execute(result);
        String nextState = stateMachine.getActiveStateName();

        System.out.println("Next state: " + nextState + " result:" + result);
        log.debug("Next state: {} result: {}", nextState, result);

        return Optional.of(new ProcessFlowResult(nextState, nextActivity));
    }

    private class ASimpleStateMachine extends StateMachine {
        {
            Action setActivity1 = () -> nextActivity = Activity.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setActivityType("activityType1")
                    .setTaskType(TaskType.AUTOMATIC)
                    .setDescription("tull") // TODO
                    .setState(ActivityState.CREATED)
                    .build();

            Action setActivity2 = () -> nextActivity = Activity.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setActivityType("activityType2")
                    .setTaskType(TaskType.MANUAL)
                    .setDescription("tull") // TODO
                    .setState(ActivityState.CREATED)
                    .build();

            State finalState = new FinalState(FINAL_STATE);

            State aSimpleState2 = state(A_SIMPLE_STATE_2)
                    .onEntry(setActivity2)
                    .transition(FINAL).guardedBy(FinalState.FINAL_EVENT)
                    .to(finalState).build();

            State aSimpleState = state(A_SIMPLE_STATE)
                    .onEntry(setActivity1)
                    .transition(A_SIMPLE_ACTION).guardedBy(A_SIMPLE_EVENT)
                    .to(aSimpleState2)
                    .build();

            addStates(Arrays.asList(aSimpleState, aSimpleState2, finalState));
            activeState(aSimpleState);
            validate();
        }
    }
}
