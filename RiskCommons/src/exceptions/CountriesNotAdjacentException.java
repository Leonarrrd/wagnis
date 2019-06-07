package exceptions;

import model.Country;

public class CountriesNotAdjacentException extends Exception {

    public CountriesNotAdjacentException(Country country1, Country country2) {
        super(country1.getName() + " is not adjacent to " + country2.getName() + ".");
    }
}
