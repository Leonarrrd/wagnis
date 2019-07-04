package persistence.helpermodels;

/**
 * Helper class that maps ids with units
 */
public class RawCountryData {

    private int id;
    private int units;

    public RawCountryData(int id, int units) {
        this.id = id;
        this.units = units;
    }

    public int getId() {
        return id;
    }

    public int getUnits() {
        return units;
    }
}
