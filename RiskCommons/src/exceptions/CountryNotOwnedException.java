package exceptions;

import model.Country;

import java.io.Serializable;

/**
 * Thrown if an action for a country is initiated but the Country that the action is associated with is not owned by the player.
 */
public class CountryNotOwnedException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public CountryNotOwnedException(Country country) {
        super(country.getName() + " is not owned by " + country.getOwner().getName() + ".");
    }
}
