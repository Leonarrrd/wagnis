package exceptions;

import java.io.Serializable;

public class InvalidNumberOfPlayersException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public InvalidNumberOfPlayersException(int amount) {
        super("Number of players is invalid (should be 2-5, is " + amount + ").");
    }

}
