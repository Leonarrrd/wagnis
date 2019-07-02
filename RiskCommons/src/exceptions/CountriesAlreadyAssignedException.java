package exceptions;

import java.io.Serializable;

/**
 * Needs to be thrown if countries were already assigned. Countries can not be assigned twice
 * and need to be assigned at the start of the game.
 */
public class CountriesAlreadyAssignedException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;


    public CountriesAlreadyAssignedException() {
        super("Countries are already assigned. Method should not be called twice for one game.");
    }
}
