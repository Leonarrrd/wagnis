package exceptions;

import model.Country;
import model.Player;

import java.io.Serializable;

public class CountryNotOwnedException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public CountryNotOwnedException(Country country) {
        super(country.getName() + " is not owned by " + country.getOwner().getName() + ".");
    }
}
