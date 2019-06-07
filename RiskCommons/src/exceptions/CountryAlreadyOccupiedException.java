package exceptions;

import model.Country;

public class CountryAlreadyOccupiedException extends Exception {

    public CountryAlreadyOccupiedException(Country country) {
        super(country.getName() + " is already occupied.");
    }
}
