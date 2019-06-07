package exceptions;

import java.util.UUID;

public class GameNotFoundException extends Exception{

    public GameNotFoundException(UUID gameId) {
        super("Game with Id " + gameId.toString() + " could not be found.");
    }

}
