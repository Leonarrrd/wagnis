package exceptions;

import model.Country;

import java.io.Serializable;

public class CountriesAlreadyAssignedException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;


    public CountriesAlreadyAssignedException() {
        super("Countries are already assigned. Method should not be called twice for one game.");
    }
}
