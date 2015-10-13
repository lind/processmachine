package org.nextstate.process.domain

import spock.lang.Specification

class ProcessTest extends Specification {

    def "create Process"() {
        when:
        def process = new Process(UUID.randomUUID().toString(), "activity", "activityType");
        then:
        process.getActivity() == "activity"
        process.getUnsavedEvents().size() == 2
    }

    def "create and lock Process"() {
        given:
        def process = new Process(UUID.randomUUID().toString(), "activity", "activityType");
        when:
        process.takeProcess("ident1")
        then:
        process.getLockedBy() == "ident1"
        process.getUnsavedEvents().size() == 3
    }

    def "create, lock and free Process"() {
        given:
        def process = new Process(UUID.randomUUID().toString(), "activity", "activityType");
        process.takeProcess("ident1")
        when:
        process.freeProcess("ident1")
        then:
        process.getProcessLockState() == ProcessLockState.OPEN
        process.getLockedBy() == null;
        process.getUnsavedEvents().size() == 4
    }

}
