package exceptions;

import model.Country;

public class CountriesNotConnectedException extends Exception {
    public CountriesNotConnectedException(Country country1, Country country2) {
        super(country1.getName() + " is not connected to " + country2);
    }
}
