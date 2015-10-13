package org.nextstate.process.domain;

public class ProcessLockedException extends RuntimeException {
    public ProcessLockedException(String lockedBy) {
        super("Process is locked by: " + lockedBy);
    }
}
