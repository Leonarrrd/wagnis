package exceptions;

import java.util.UUID;

public class DuplicateGameIdException extends Exception{

    public DuplicateGameIdException(UUID gameId) {
        super("Game with id " + gameId.toString() + " already exists.");
    }
}
