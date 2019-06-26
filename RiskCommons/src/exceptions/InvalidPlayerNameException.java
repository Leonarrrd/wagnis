package exceptions;

import java.io.Serializable;

public class InvalidPlayerNameException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public InvalidPlayerNameException(String playerName) {
        super(playerName + " does not meet the conventions.");
    }
}
