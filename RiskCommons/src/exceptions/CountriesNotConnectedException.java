package exceptions;

import model.Country;

import java.io.Serializable;

public class CountriesNotConnectedException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public CountriesNotConnectedException(Country country1, Country country2) {
        super(country1.getName() + " is not connected to " + country2);
    }
}
