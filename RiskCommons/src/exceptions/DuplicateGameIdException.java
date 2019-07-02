package exceptions;

import java.io.Serializable;
import java.util.UUID;

/**
 * Thrown if there are games with a duplicate Id.
 */
public class DuplicateGameIdException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public DuplicateGameIdException(UUID gameId) {
        super("Game with id " + gameId.toString() + " already exists.");
    }
}
