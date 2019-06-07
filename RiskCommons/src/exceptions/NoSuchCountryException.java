package exceptions;

import model.Country;

public class NoSuchCountryException extends Exception {

    public NoSuchCountryException(Country country) {
        super("Country " + country.getName() + " should not exist.");
    }
}
