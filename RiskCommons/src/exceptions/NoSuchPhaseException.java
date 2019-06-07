package exceptions;

import datastructures.Phase;

public class NoSuchPhaseException extends Exception {

    public NoSuchPhaseException(Phase phase) {
        super("Phase " + phase.name() + " should not exist.");
    }
}
