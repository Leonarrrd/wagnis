package exceptions;

public class CardAlreadyOwnedException extends Exception {
    public CardAlreadyOwnedException(String message) {
        super(message);
    }
}
