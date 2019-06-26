package exceptions;

import java.io.Serializable;
import java.util.UUID;

public class DuplicateGameIdException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public DuplicateGameIdException(UUID gameId) {
        super("Game with id " + gameId.toString() + " already exists.");
    }
}
