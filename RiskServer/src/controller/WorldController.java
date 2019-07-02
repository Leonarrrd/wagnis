package controller;

import exceptions.*;
import model.Country;
import model.Game;
import model.Player;

import java.util.*;

/**
 * Controller that handles world mechanisms (countries, etc.)
 */
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
     * @see interfaces.IGameController#changeUnits(UUID, Country, int)
     */
    void changeUnits(Game game, Country country, int units) throws NoSuchCountryException {
        if (!game.getCountries().containsValue(country)) {
            throw new NoSuchCountryException(country);
        }
        int tempUnits = game.getCountries().get(country.getName()).getUnits();
        game.getCountries().get(country.getName()).setUnits(tempUnits + units);
    }

     /**
     * Assigns all countries to players randomly
     * Only used in simple game mode, for normal game mode, we let people choose their own country, see UI
     */
    void assignCountries(Game game) throws CountriesAlreadyAssignedException {
        List<Country> countries = new ArrayList<>(game.getCountries().values());
        boolean assigned = false;
        for (Country c : countries) {
            if (c.getOwner() != null) {
                assigned = true;
            }
        }

        if (assigned) {
            throw new CountriesAlreadyAssignedException();
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
     * Looks at all neighbors the country has and creates a map of the ones that are not occupied by the player occupying it
     * Used to determine eligible attack destinations
     *
     * @param country
     * @return
     */
    Map<String, Country> getHostileNeighbors(Game game, Country country) throws NoSuchCountryException {
        if (!game.getCountries().containsValue(country)) {
            throw new NoSuchCountryException(country);
        }
        Map<String, Country> hostileNeighbors = new HashMap<>();
        for (Country neighborCountry : country.getNeighbors()) {
            if (!(country.getOwner().getName().equals(neighborCountry.getOwner().getName()))) { // maybe better to implement equals for player
                hostileNeighbors.put(neighborCountry.getName(), neighborCountry);
            }
        }
        return hostileNeighbors;
    }

    /**
     * @param country to get allied neighbours from
     * @return Map of allied neighbours
     */
    Map<String, Country> getAlliedNeighbors(Game game, Country country) throws NoSuchCountryException {
        if (!game.getCountries().containsValue(country)) {
            throw new NoSuchCountryException(country);
        }
        Map<String, Country> alliedNeighbors = new HashMap<>();
        for (Country neighborCountry : country.getNeighbors()) {
            if (country.getOwner().getName().equals(neighborCountry.getOwner().getName())) {
                alliedNeighbors.put(neighborCountry.getName(), neighborCountry);
            }
        }
        return alliedNeighbors;
    }

    /**
     * Used to determine move countries
     *
     * @param player
     * @return
     */
    Map<String, Country> getCountriesWithMoreThanOneUnit(Game game, Player player) throws NoSuchPlayerException {
        if (!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
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
     * @see interfaces.IGameController#moveUnits(UUID, Country, Country, int, boolean)
     */
    void moveUnits(Game game, Country srcCountry, Country destCountry, int amount) throws NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotConnectedException {
        if (!game.getCountries().values().contains(srcCountry)) {
            throw new NoSuchCountryException(srcCountry);
        }
        if (!game.getCountries().values().contains(destCountry)) {
            throw new NoSuchCountryException(destCountry);
        }
        if (srcCountry.getUnits() < amount) {
            throw new NotEnoughUnitsException(srcCountry);
        }
        if (!srcCountry.getOwner().equals(destCountry.getOwner())) {
            throw new CountryNotOwnedException(srcCountry);
        }
        if(!isConnected(srcCountry, destCountry)) {
            throw new CountriesNotConnectedException(srcCountry, destCountry);
        }

        srcCountry.setUnits(srcCountry.getUnits() - amount);
        destCountry.setUnits(destCountry.getUnits() + amount);
    }

    /**
     * Checks if two countries are connected
     *
     * @param srcCountry source country
     * @param destCountry destination country
     */
    boolean isConnected(Country srcCountry, Country destCountry) {
        return srcCountry.getOwner().getCountryGraph().evaluateCountriesAllowedToMoveTo(srcCountry.getName()).contains(destCountry.getName());
    }


    /**
     * changing the ownership of a country
     *
     * @param defendingCountry
     * @param attackingCountry
     */
    void changeCountryOwnership(Game game, Country attackingCountry, Country defendingCountry) throws NoSuchCountryException {
        if (!game.getCountries().containsValue(attackingCountry)) {
            throw new NoSuchCountryException(attackingCountry);
        }
        if (!game.getCountries().containsValue(defendingCountry)) {
            throw new NoSuchCountryException(defendingCountry);
        }

        defendingCountry.getOwner().getCountries().remove(defendingCountry.getName());
        defendingCountry.setOwner(attackingCountry.getOwner());
        attackingCountry.getOwner().getCountries().put(defendingCountry.getName(), defendingCountry);
    }
}
