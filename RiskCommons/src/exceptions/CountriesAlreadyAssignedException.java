package exceptions;

import model.Country;

public class CountriesAlreadyAssignedException extends Exception {

    public CountriesAlreadyAssignedException() {
        super("Countries are already assigned. Method should not be called twice for one game.");
    }
}
