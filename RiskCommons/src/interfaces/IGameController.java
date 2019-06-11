package interfaces;

import datastructures.CardBonus;
import exceptions.*;
import model.*;

import java.io.IOException;
import java.util.*;

public interface IGameController {

    /**
     * Gets the game from the active game map by providing an ID
     *
     * @param id Id to identify the game
     * @return The active Game-Object
     * @throws GameNotFoundException if the game could not be found with the provided Id
     */
    Game getGameById(UUID id) throws GameNotFoundException;

    /**
     * Initalizes a new game
     *
     * @return The newly created game object.
     * @throws IOException If files needed to load the game could not be found (e.g. the country.dat file)
     * @throws InvalidFormattedDataException If files that are needed are saved in the wrong format (e.g. the country.dat file)
     */
    Game initNewGame() throws IOException, InvalidFormattedDataException;

    /**
     * loads a saved game
      * @param gameId
     * @return
     * @throws IOException
     * @throws GameNotFoundException
     * @throws InvalidFormattedDataException
     */
    Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException;

    void addPlayer(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException;

    void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException;

    void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException;

    void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException;

    void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException;


    void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException;


    int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException;

    void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;

    void addCardsToDeck(UUID gameId, List<Card> cards) throws GameNotFoundException;

    CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards);


    void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException;

    void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException;


    void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException;

    Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;

    Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException;


    boolean hasCountryToMoveFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;


    boolean hasCountryToAttackFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;

    Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws GameNotFoundException, NoSuchCountryException;

    /**
     * Carries out one round of an attack
     * Rolls the dices and deducts units from the countries depending on the roll's outcome
     * The method does NOT handle invalid parameters. The instance that calls this function must ensure the parameters are valid
     * The method is rather long and might be rewritten in a more elegant way
     * Probably better to split this method in 2-3 smaller methods, with dice rolls, unit deducts, and checking for a winner separate
     *
     * @param attackingCountry the country to attack from
     * @param defendingCountry the country that is defending the attack
     * @param attackingUnits the amount of units the attacking country attacks with
     * @param defendingUnits the amount of units the defending country defends with
     * @return null if both sides still have enough units to fight, the winner of the war otherwise
     * @throws NotEnoughUnitsException if the country does not have enough units to attack or defend with
     * @throws CountriesNotAdjacentException if the countries that should be fighting are not adjacent to each other
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchCountryException if the given countries do not exist
     */
    AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException;

    void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotAdjacentException;


    boolean checkWinCondidtion(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, IOException;

    void setTurn(UUID gameId) throws GameNotFoundException;

    void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException;

    void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException;

    void postPhaseCheck(UUID gameId, Turn turn) throws GameNotFoundException, IOException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;

    boolean isConnected(Country srcCountry, Country destCountry);

    void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException;

    void removeGame(UUID gameId) throws GameNotFoundException, IOException;

    List<String> loadAvailableGameIds() throws IOException;


}
