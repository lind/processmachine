package org.nextstate.process.domain

import spock.lang.Ignore
import spock.lang.Specification

class ProcessSpec extends Specification {

    def "create Process"() {
        when:
        def process = new Process(UUID.randomUUID().toString(), "processType", "activityType", "AUTOMATIC");
        then:
        process.getProcessType() == "processType"
        process.getUnsavedEvents().size() == 1 // create process
//        process.getActivity().getActivityType() == "activityType"
//        process.getActivity().getTaskType() == TaskType.AUTOMATIC
//        process.getActivity().getState() == ActivityState.CREATED

    }

    @Ignore // activity not created by default anymore
    def "create and take process. Process locked and Activity state: In progress"() {
        given:
        def process = new Process(UUID.randomUUID().toString(), "processType", "activity", "AUTOMATIC");
        when:
        process.takeProcess("ident1")
        then:
        process.getProcessLockState() == ProcessLockState.LOCKED
        process.getLockedBy() == "ident1"
        process.getUnsavedEvents().size() == 2
//        process.getActivity().getState() == ActivityState.IN_PROGRESS
    }

    @Ignore // activity not created by default anymore
    def "create, take process and complete activity. Process open and activity state: completed"() {
        given:
        def process = new Process(UUID.randomUUID().toString(), "processType", "activity", "AUTOMATIC");
        process.takeProcess("ident1")
        when:
        process.activityCompleted("ident1", "result")
        then:
        process.getProcessLockState() == ProcessLockState.OPEN
        process.getLockedBy() == null;
        process.getUnsavedEvents().size() == 3
//        process.getActivity().getState() == ActivityState.COMPLETED
    }

}
