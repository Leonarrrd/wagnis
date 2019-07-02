package exceptions;

import datastructures.Phase;

import java.io.Serializable;

/**
 * MARK: Not used yet but might be useful later
 * Thrown if there is no such phase in the game
 */
public class NoSuchPhaseException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NoSuchPhaseException(Phase phase) {
        super("Phase " + phase.name() + " should not exist.");
    }
}
