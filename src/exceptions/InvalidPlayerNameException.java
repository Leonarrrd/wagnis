package exceptions;

public class InvalidPlayerNameException extends Exception {
    public InvalidPlayerNameException(String message) {
        super(message);
    }
}
