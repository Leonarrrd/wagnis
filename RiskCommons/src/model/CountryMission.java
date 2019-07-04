package model;

import java.io.Serializable;

/**
 * Mission Subclass for Country win conditions
 * Extends abstract class mission and has a custom isAccomplished()-Method
 * that checks if the mission is accomplished
 */
public class CountryMission extends Mission implements Serializable {

    private static final long serialVersionUID = 1L;
    private int countriesToConquer;

    public CountryMission(int id, int countriesToConquer) {
        super(id);
        this.countriesToConquer = countriesToConquer;
    }

    public boolean isAccomplished(Player owner, Game game) {
        return owner.getCountries().size() >= countriesToConquer;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Conquer ");
        sb.append(countriesToConquer);
        sb.append(" Countries.");
        return sb.toString();
    }
}
