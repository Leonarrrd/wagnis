package exceptions;

import model.Country;
import model.Player;

public class CountryNotOwnedException extends Exception {
    public CountryNotOwnedException(Country country) {
        super(country.getName() + " is not owned by " + country.getOwner().getName() + ".");
    }
}
