package exceptions;

import model.Country;

import java.io.Serializable;

public class CountriesNotAdjacentException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;


    public CountriesNotAdjacentException(Country country1, Country country2) {
        super(country1.getName() + " is not adjacent to " + country2.getName() + ".");
    }
}
