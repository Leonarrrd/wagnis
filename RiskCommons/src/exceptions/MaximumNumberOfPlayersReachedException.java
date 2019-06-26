package exceptions;

import java.io.Serializable;

public class MaximumNumberOfPlayersReachedException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public MaximumNumberOfPlayersReachedException(int playerAmount) {
        super("Maximum number of players (5) reached. There are currently " + playerAmount + " players in the game.");
    }
}
