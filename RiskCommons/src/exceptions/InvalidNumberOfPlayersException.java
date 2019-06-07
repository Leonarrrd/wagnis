package exceptions;

public class InvalidNumberOfPlayersException extends Exception {

    public InvalidNumberOfPlayersException(int amount) {
        super("Number of players is invalid (should be 2-5, is " + amount + ").");
    }

}
