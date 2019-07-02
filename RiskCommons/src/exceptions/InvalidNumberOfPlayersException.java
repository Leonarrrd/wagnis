package exceptions;

import java.io.Serializable;

/**
 * Thrown if number of players is not between 2 and 5.
 */
public class InvalidNumberOfPlayersException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public InvalidNumberOfPlayersException(int amount) {
        super("Number of players is invalid (should be 2-5, is " + amount + ").");
    }

}
