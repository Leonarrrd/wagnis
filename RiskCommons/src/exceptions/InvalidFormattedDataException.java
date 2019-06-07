package exceptions;

public class InvalidFormattedDataException extends Exception {
    public InvalidFormattedDataException() {
        super("Data is not in the right format.");
    }
}
