package org.nextstate.process.domain

import org.nextstate.process.activity.ActivityState
import org.nextstate.process.activity.TaskType
import spock.lang.Specification

class ProcessWithASimpleFlowSpec extends Specification {

    def "create Process"() {
        when:
        def process = new ProcessWithASimpleFlow(UUID.randomUUID().toString(), "processType", "activityType", "AUTOMATIC");
        then:
        process.getProcessType() == "processType"
        process.getUnsavedEvents().size() == 2 // create process and create activity
        process.getActivity().getActivityType() == "activityType1"
        process.getActivity().getTaskType() == TaskType.AUTOMATIC
        process.getActivity().getState() == ActivityState.CREATED
        process.getProcessState() == ProcessWithASimpleFlow.A_SIMPLE_STATE
    }

    def "activity completed and new process state"() {
        given:
        def process = new ProcessWithASimpleFlow(UUID.randomUUID().toString(), "processType", "activity", "AUTOMATIC");
        process.takeProcess("ident1")
        when:
        process.activityCompleted("ident1", ProcessWithASimpleFlow.A_SIMPLE_EVENT)
        then:
        process.getProcessLockState() == ProcessLockState.OPEN
        process.getLockedBy() == null;
        process.getUnsavedEvents().size() == 5
        process.getProcessState() == ProcessWithASimpleFlow.A_SIMPLE_STATE_2
        process.getActivity().getActivityType() == "activityType2"
        process.getActivity().getState() == ActivityState.CREATED
    }

}
