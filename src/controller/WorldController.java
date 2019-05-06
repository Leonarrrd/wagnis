package controller;

import exceptions.*;
import model.Country;
import model.Game;
import model.Player;

import java.util.*;

public class WorldController {

    private static WorldController instance;

    private WorldController() {
    }

    static WorldController getInstance() {
        if (instance == null) {
            instance = new WorldController();
        }

        return instance;
    }

    /**
     * @param countryAsString
     * @param player
     */
    void addCountry(Game game, String countryAsString, Player player) throws CountryAlreadyOccupiedException {
        Country country = game.getCountries().get(countryAsString);

        if (country.getOwner() != null) {
            throw new CountryAlreadyOccupiedException("The country " + countryAsString + "is already occupied. A country can only be owned by one player.");
        }

        player.getCountries().put(countryAsString, country);
        country.setOwner(player);
    }

    /**
     * Add units to the specified country
     *
     * @param country
     * @param units   units to add
     */
    void changeUnits(Game game, Country country, int units) {
        int tempUnits = game.getCountries().get(country.getName()).getUnits();
        game.getCountries().get(country.getName()).setUnits(tempUnits + units);
    }

    void changeFrozenUnits(Game game, Country country, int frozenUnits) {

        int tempFrozenUnits = game.getCountries().get(country.getName()).getUnits();
        game.getCountries().get(country.getName()).setFrozenUnits(tempFrozenUnits + frozenUnits);
    }

    /**
     * Assigns all countries to players randomly
     * Only used in simple game mode, for normal game mode, we let people choose their own country, see UI
     */
    void assignCountries(Game game) throws CountriesAlreadyAssignedException {
        List<Country> countries = new ArrayList<>(game.getCountries().values());
        boolean assigned = false;
        for(Country c : countries) {
            if(c.getOwner() != null) {
                assigned = true;
            }
        }

        if(assigned) {
            throw new CountriesAlreadyAssignedException("Countries already have been assigned.");
        }

        Collections.shuffle(countries);
        int p = 0;

        for (Country country : countries) {
            Player player = game.getPlayers().get(p);
            player.getCountries().put(country.getName(), country);
            country.setOwner(player);
            country.setUnits(5); //TODO: put 10 for testing purposes
            if (++p > game.getPlayers().size() - 1) {
                p = 0;
            }
        }
    }

    /**
     * Assigns a number of starting units to players depending on the number of players
     *
     * @return the amount of units assigned
     */
    int assignUnits(Game game) throws InvalidNumberOfPlayersException {

        int startingUnits;
        switch (game.getPlayers().size()) {
            case 2:
                startingUnits = 5; //TODO: its actually 40, just put 5 for testing
                break;
            case 3:
                startingUnits = 35;
                break;
            case 4:
                startingUnits = 30;
                break;
            case 5:
                startingUnits = 25;
                break;
            default:
                throw new InvalidNumberOfPlayersException("Invalid number of players");
        }
        return startingUnits;
    }

    /**
     * Returns a map not a list so we can print keys in the interface more easily
     * Map contains countries that have: a) More than one unit; b) at least one hostile neighbour
     * Used to determine eligible source attack and countries
     * TODO: inconsistent naming with method below?
     *
     * @param player
     * @return map of countries with more than one unit
     */
    Map<String, Country> getCountriesAttackCanBeLaunchedFrom(Game game, Player player)  throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }


        Map<String, Country> countriesWithMoreThanOneUnit = new HashMap<>();
        for (Country country : player.getCountries().values()) {
            if (country.getUnits() > 1 && !getHostileNeighbors(country).isEmpty()) {
                countriesWithMoreThanOneUnit.put(country.getName(), country);
            }
        }
        return countriesWithMoreThanOneUnit;
    }

    /**
     * TODO: Same concerns as above
     * Looks at all neighbors the country has and creates a map of the ones that are not occupied by the player occupying it
     * Used to determine eligible attack destinations
     *
     * @param country
     * @return
     */
    Map<String, Country> getHostileNeighbors(Country country) {
        Map<String, Country> hostileNeighbors = new HashMap<>();
        for (Country neighborCountry : country.getNeighbors()) {
            if (!(country.getOwner().getName().equals(neighborCountry.getOwner().getName()))) { // maybe better to implement equals for player
                hostileNeighbors.put(neighborCountry.getName(), neighborCountry);
            }
        }
        return hostileNeighbors;
    }

    /**
     * @param country
     * @return
     */
    Map<String, Country> getAlliedNeighbors(Country country) {
        Map<String, Country> alliedNeighbors = new HashMap<>();
        for (Country neighborCountry : country.getNeighbors()) {
            if (country.getOwner().getName().equals(neighborCountry.getOwner().getName())) {
                alliedNeighbors.put(neighborCountry.getName(), neighborCountry);
            }
        }
        return alliedNeighbors;
    }

    /**
     * TODO:Same as above just that country doesnt need to have hostile neighbors
     * Used to determine move countries
     *
     * @param player
     * @return
     */
    Map<String, Country> getCountriesWithMoreThanOneUnit(Game game, Player player) throws NoSuchPlayerException{
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }
        Map<String, Country> countriesWithMoreThanOneUnit = new HashMap<>();
        for (Country country : player.getCountries().values()) {
            if (country.getUnits() > 1) {
                countriesWithMoreThanOneUnit.put(country.getName(), country);
            }
        }
        return countriesWithMoreThanOneUnit;
    }



    /**
     * TODO: We should use changeUnits() here, but we have no gameID
     *
     * @param srcCountry
     * @param destCountry
     * @param amount
     */
    void moveUnits(Game game, Country srcCountry, Country destCountry, int amount) throws NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException {
        if(!game.getCountries().values().contains(srcCountry)) {
            throw new NoSuchCountryException(srcCountry + " does not exist.");
        }
        if(!game.getCountries().values().contains(destCountry)) {
            throw new NoSuchCountryException(destCountry + " does not exist.");
        }
        if (srcCountry.getUnits() < amount) {
            throw new NotEnoughUnitsException(srcCountry.getName() + " does not have enough units to move.");
        }
        if (!srcCountry.getOwner().equals(destCountry.getOwner())) {
            throw new CountryNotOwnedException("The owner of " + srcCountry + " does not equal the owner of " + destCountry);
        }

        srcCountry.setUnits(srcCountry.getUnits() - amount);
        destCountry.setUnits(destCountry.getUnits() + amount);
    }
}
