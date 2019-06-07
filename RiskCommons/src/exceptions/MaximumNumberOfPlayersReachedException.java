package exceptions;

public class MaximumNumberOfPlayersReachedException extends Exception{

    public MaximumNumberOfPlayersReachedException(int playerAmount) {
        super("Maximum number of players (5) reached. There are currently " + playerAmount + " players in the game.");
    }
}
