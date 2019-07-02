package exceptions;

import java.io.Serializable;
import java.util.UUID;

/**
 * Thrown if the game with the provided Id could not be found.
 */
public class GameNotFoundException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;


    public GameNotFoundException(UUID gameId) {
        super("Game with Id " + gameId.toString() + " could not be found.");
    }

}
