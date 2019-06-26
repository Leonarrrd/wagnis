package exceptions;

import model.Country;

import java.io.Serializable;

public class CountryAlreadyOccupiedException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public CountryAlreadyOccupiedException(Country country) {
        super(country.getName() + " is already occupied.");
    }
}
