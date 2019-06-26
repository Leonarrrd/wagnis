package exceptions;

import model.Country;

import java.io.Serializable;

public class NoSuchCountryException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;


    public NoSuchCountryException(Country country) {
        super("Country " + country.getName() + " should not exist.");
    }
}
