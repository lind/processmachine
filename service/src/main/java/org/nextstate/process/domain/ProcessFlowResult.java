package org.nextstate.process.domain;

import org.nextstate.process.activity.Activity;

public class ProcessFlowResult {
    private final String processState;
    private final Activity nextActivity;

    public ProcessFlowResult(String processState, Activity nextActivity) {
        this.processState = processState;
        this.nextActivity = nextActivity;
    }

    public String getProcessState() {
        return processState;
    }

    public Activity getNextActivity() {
        return nextActivity;
    }
}
