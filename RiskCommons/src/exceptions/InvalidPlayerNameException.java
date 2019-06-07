package exceptions;

public class InvalidPlayerNameException extends Exception {
    public InvalidPlayerNameException(String playerName) {
        super(playerName + " does not meet the conventions.");
    }
}
