package exceptions;

import model.Country;

import java.io.Serializable;

/**
 * Thrown if there are not enough units to perform the action
 */
public class NotEnoughUnitsException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NotEnoughUnitsException(Country country) {
        super(country.getName() + " does not have enough units (" + country.getUnits() + ").");
    }
}
