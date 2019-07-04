package view.gui.viewhelper;

import model.Country;

public class LastFightCountries {
    private Country srcCountry;
    private Country destCountry;

    public LastFightCountries(Country srcCountry, Country destCountry) {
        this.srcCountry = srcCountry;
        this.destCountry = destCountry;
    }

    public Country getSrcCountry() {
        return srcCountry;
    }

    public Country getDestCountry() {
        return destCountry;
    }

}
