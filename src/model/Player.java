package model;
import datastructures.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Class  for player objects
 */
public class Player {
    private String name;
    private Color color;
    private Map<String, Country> countries = new HashMap<>(); // maybe better as Map<String, Country>?

    private List<Card> cards = new ArrayList<>();

    private Mission mission;
    private boolean hasConqueredCountry = false;
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }
    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission){
        this.mission = mission;
    }

    public Map<String, Country> getCountries(){
        return countries;
    }

    public void setCountries(Map<String, Country> countries) {
        this.countries = countries;
    }

    public List<Card> getCards(){
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean hasConqueredCountry() {
        return hasConqueredCountry;
    }

    public void setHasConqueredCountry(boolean hasConqueredCountry) {
        this.hasConqueredCountry = hasConqueredCountry;
    }
}
