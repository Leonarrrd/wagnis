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
     * Loads a saved game
     * @param gameId The game Id of the game that should be loaded. Can be allocated
     *               by the method loadAvailableGameIds()
     * @return
     * @throws IOException if the saved game File could not be read from the file system
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws InvalidFormattedDataException if the saved game file is corrupted
     */
    Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException;

    /**
     * Methods that adds a player to the game
     *
     * @param gameId The game Id of the game that the player should be added to
     * @param playerName The name the player should have
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws MaximumNumberOfPlayersReachedException if there are already enough players added
     * @throws InvalidPlayerNameException if the Name uses delimitters or other invalid characters
     */
    void addPlayer(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException;

    /**
     * TODO: add comments
     * FIXME: Kann die Methode weg oder ist das kunst?
     *
     * @param gameId
     * @param countryAsString
     * @param player
     * @throws GameNotFoundException
     * @throws CountryAlreadyOccupiedException
     * @throws NoSuchCountryException
     */
    void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException;

    /**
     * Changes the units from a country by providing the Country-Object and the units
     *
     * @param gameId The game Id of the game where the units should be changed
     * @param country The country object the units should be added to.
     * @param units The units that should be added or substracted. Substract by providing a negative Integer.
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws NoSuchCountryException if the country the units should be added to does not exist
     */
    void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException;

    /**
     * TODO: Add commentary
     * FIXME: Kann weg oder?
     *
     * @param gameId
     * @param country
     * @param frozenUnits
     * @throws GameNotFoundException
     * @throws NoSuchCountryException
     */
    void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException;

    /**
     * Method that assignes countries to the players at the start of the game
     *
     * @param gameId needs to be provided at the start of the game.
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws CountriesAlreadyAssignedException Countries cannot be assigned twice, if called twice, this exception is thrown.
     */
    void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException;

    /**
     * Assigns missions to the players at the start of the game
     * @param gameId needs to be provided to identify the correct game
     *
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws MaximumNumberOfPlayersReachedException if there are too many players to assign missions to, this exception is thrown
     */
    void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException;

    /**
     * TODO: add commentary
     * FIXME: kann weg oder?
     *
     * @param gameId
     * @return
     * @throws GameNotFoundException
     * @throws InvalidNumberOfPlayersException
     */
    int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException;

    /**
     * Adds a card to a player after a successful attack
     *
     * @param gameId needs to be provided to identify the correct game
     * @param player the player that the cards needed to
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws NoSuchPlayerException if the player does not exist
     * @throws NoSuchCardException if the card with the provided id does not exist
     * @throws CardAlreadyOwnedException if the player already owns this exact card
     */
    void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;

    /**
     * Adds cards to the Card Deck that is "drawn" from
     *
     * @param gameId needs to be provided to identify the correct game
     * @param cards the list of cards that need to be added to the Deck
     * @throws GameNotFoundException if the game with the provided Id could not be found
     */
    void addCardsToDeck(UUID gameId, List<Card> cards) throws GameNotFoundException;

    /**
     * Gets the right values for the provided cards if a player decides to use his cards
     *
     * @param infantryCards the amount of infantry cards provided by the player
     * @param cavalryCards the amount of cavalry cards provided by the player
     * @param artilleryCards the amount of artillery cards provided by the player
     * @return CardBonus-Object that holds the values for the cards
     */
    CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards);

    /**
     * Awards units to the player
     * Called at the start of each Turn for the provided player
     *
     * @param gameId needs to be provided to identify the correct game
     * @param player the player that should be awarded units
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws NoSuchPlayerException if the player to award units to does not exist
     */
    void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException;

    /**
     * Changes the units that can be placed by a player
     * e.g. when using cards beforehand
     *
     * @param gameId needs to be provided to identify the correct game
     * @param player the player that places units
     * @param unitChange the units that are added / substracted
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws NoSuchPlayerException
     */
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
