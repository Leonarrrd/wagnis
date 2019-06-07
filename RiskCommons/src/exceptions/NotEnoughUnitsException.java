package exceptions;

import model.Country;

public class NotEnoughUnitsException extends Exception {

    public NotEnoughUnitsException(Country country) {
        super(country.getName() + " does not have enough units (" + country.getUnits() + ").");
    }
}
