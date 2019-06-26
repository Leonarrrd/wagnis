package exceptions;

import datastructures.Phase;

import java.io.Serializable;

public class NoSuchPhaseException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NoSuchPhaseException(Phase phase) {
        super("Phase " + phase.name() + " should not exist.");
    }
}
