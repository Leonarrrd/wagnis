package exceptions;

import model.Country;

import java.io.Serializable;

/**
 * Thrown if a country is already occupied and the player wants to occupy it again.
 */
public class CountryAlreadyOccupiedException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public CountryAlreadyOccupiedException(Country country) {
        super(country.getName() + " is already occupied.");
    }
}
