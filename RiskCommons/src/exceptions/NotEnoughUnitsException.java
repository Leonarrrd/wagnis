package exceptions;

import model.Country;

import java.io.Serializable;

public class NotEnoughUnitsException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NotEnoughUnitsException(Country country) {
        super(country.getName() + " does not have enough units (" + country.getUnits() + ").");
    }
}
