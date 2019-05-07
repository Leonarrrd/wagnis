package exceptions;

public class DuplicateGameIdException extends Exception{

    public DuplicateGameIdException(String message) {
        super(message);
    }
}
