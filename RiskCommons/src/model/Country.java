package model;

import java.io.Serializable;
import java.util.*;

/*
 * Class for Country objects
 */
public class Country implements Serializable {
    private int id;
    private String name;
    private Player owner;
    private int units;
    private int frozenUnits;
    private List<Country> neighbors = new ArrayList<>();

    //TODO: Constructor might be redundant
    public Country(String name) {
        this.name = name;
    }

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getFrozenUnits() {
        return frozenUnits;
    }

    public void setFrozenUnits(int frozenUnits) {
        this.frozenUnits = frozenUnits;
    }

    public List<Country> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Country> neighbors) {
        this.neighbors = neighbors;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Country) {
            Country c = (Country) obj;
            if (this.name.equals(c.getName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String printRepresantation() {
        return "Name: " + this.name + " Owner: " + this.owner + " Units: " + this.units;
    }

}
