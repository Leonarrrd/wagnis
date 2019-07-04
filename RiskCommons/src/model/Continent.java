package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Continent class that specifies the name, an id, the countries in this continent
 * and the bonus units when owning this continent.
 */
public class Continent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int bonusUnits;
    private List<Country> countries = new ArrayList<>();

    public Continent(int id, String name, int bonusUnits, List<Country> countries) {
        this.id = id;
        this.name = name;
        this.countries = countries;
        this.bonusUnits = bonusUnits;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBonusUnits() {
        return bonusUnits;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public boolean isOwnedByPlayer(Player player) {
        boolean isOwned = true;
        for (Country country : countries) {
            if (country.getOwner() != player) {
                isOwned = false;
            }
        }
        return isOwned;
    }

    @Override
    public String toString() {
        return name + " : " + countries;
    }
}
