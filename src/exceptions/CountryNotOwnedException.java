package exceptions;

public class CountryNotOwnedException extends Exception {
    public CountryNotOwnedException(String message) {
        super(message);
    }
}
