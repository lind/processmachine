package org.nextstate.process.application;

public class MissingProcessException extends RuntimeException {
    public MissingProcessException(String id) {
        super("No process with id:" + id);
    }
}
