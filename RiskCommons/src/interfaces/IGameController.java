package interfaces;

import datastructures.Color;
import datastructures.Phase;
import exceptions.*;
import model.*;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Interfaces the provides all the methodes that control the Game state / Game flow
 * Implemented by the client as Facade for socket requests and Server to handle the request and change the game state
 */
public interface IGameController {


    /**
     * Create a new game while joining the first Player
     *
     * @param gameId The game Id - can be passed as a new random UUID
     * @param hostPlayerName The name of the first player in this game
     * @param socket The socket of the host Player
     *
     * @throws InvalidPlayerNameException
     */
    void createGameRoom(UUID gameId, String hostPlayerName, Socket socket) throws InvalidPlayerNameException, IOException;


    /**
     * Gets the game from the active game map by providing an ID
     *
     * @param id Id to identify the game
     * @return The active Game-Object
     * @throws GameNotFoundException if the game could not be found with the provided Id
     */
    Game getGameById(UUID id) throws GameNotFoundException, IOException, ClassNotFoundException;

    /**
     * Initalizes a new game
     *
     * @return The newly created game object.
     * @throws IOException If files needed to load the game could not be found (e.g. the country.dat file)
     * @throws InvalidFormattedDataException If files that are needed are saved in the wrong format (e.g. the country.dat file)
     */
    void initNewGame(UUID gameId) throws IOException, InvalidFormattedDataException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException, CountriesAlreadyAssignedException, GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;

    /**
     * Loads a saved game
     *
     * @param gameId The game Id of the game that should be loaded. Can be allocated
     *               by the method loadAvailableGameIds()
     * @return
     * @throws IOException if the saved game File could not be read from the file system
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws InvalidFormattedDataException if the saved game file is corrupted
     */
    Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException, ClassNotFoundException, NoSuchPlayerException;

    /**
     * Methods that adds a player to the game
     *
     * @param gameId The game Id of the game that the player should be added to
     * @param playerName The name the player should have
     * @param color The player's color
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws MaximumNumberOfPlayersReachedException if there are already enough players added
     * @throws InvalidPlayerNameException if the Name uses delimitters or other invalid characters
     */
    void addPlayer(UUID gameId, String playerName, Color color) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException, IOException;


    /**
     * Changes the units from a country by providing the Country-Object and the units
     *
     * @param gameId The game Id of the game where the units should be changed
     * @param country The country object the units should be added to.
     * @param units The units that should be added or substracted. Substract by providing a negative Integer.
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws NoSuchCountryException if the country the units should be added to does not exist
     */
    void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException, IOException;


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
    void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException;


    /**
     * Changes the units that can be placed by a player
     * e.g. when using cards beforehand
     *
     * @param gameId needs to be provided to identify the correct game
     * @param player the player that places units
     * @param unitChange the units that are added / substracted
     * @throws GameNotFoundException if the game with the provided Id could not be found
     * @throws NoSuchPlayerException if the player does not exist
     */
    void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException, IOException;


    /**
     * Method for using cards at the start of the turn
     *
     * @param gameId needs to be provided to identify the correct game
     * @param player the player that uses the cards
     * @param infantryCards amount of infantry cards
     * @param cavalryCards amount of infantry cards
     * @param artilleryCards amount of infantry cards
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchCardException if the cards that wants to be used does not exist
     * @throws NoSuchPlayerException if the player that uses the cards does not exist
     * @throws IOException if a socket exception occurs
     */
    void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException, IOException;


    /**
     * Method that initialises an attack
     *
     * @param gameId needs to be provided to identify the correct game
     * @param attackingCountry the country that starts the attack
     * @param defendingCountry the country that is being attacked
     * @param units the amount of units that is attacked with
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchCountryException if the country does not exist
     * @throws IOException if a socket exception occurs
     */
    void initAttack(UUID gameId, String attackingCountry, String defendingCountry, int units) throws  GameNotFoundException, NoSuchCountryException, IOException;

    /**
     * Carries out one round of an attack
     * Rolls the dices and deducts units from the countries depending on the roll's outcome
     * The method does NOT handle invalid parameters. The instance that calls this function must ensure the parameters are valid
     * The method is rather long and might be rewritten in a more elegant way
     * Probably better to split this method in 2-3 smaller methods, with dice rolls, unit deducts, and checking for a winner separate
     *
     * @param gameId needs to be provided to identify the correct game
     * @param attackingCountry the country to attack from
     * @param defendingCountry the country that is defending the attack
     * @param attackingUnits the amount of units the attacking country attacks with
     * @param defendingUnits the amount of units the defending country defends with
     * @throws NotEnoughUnitsException if the country does not have enough units to attack or defend with
     * @throws CountriesNotAdjacentException if the countries that should be fighting are not adjacent to each other
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchCountryException if the given countries do not exist
     * @return An AttackResult-Object that includes the dices rolled and the winner
     */
    AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException, IOException, ClassNotFoundException;

    /**
     * Method to move methods from one friendly country to another connected
     *
     * @param gameId needs to be provided to identify the correct game
     * @param srcCountry the country that wants to be moved from
     * @param destCountry the country that wants to be moved to
     * @param amount the amount of units that are being moved
     * @param trail Trailing units also works for this method. Flag if it's for trailing units or moving. false = move, true = trail untis
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NotEnoughUnitsException if there are not enough units to move
     * @throws CountryNotOwnedException if the source country or destination country is not owned by the player that wants to move
     * @throws NoSuchCountryException if the source country or destination country does not exist
     * @throws CountriesNotConnectedException if the countries are not connected
     * @throws IOException if a socket exception occurs
     */
    void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount, boolean trail) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotConnectedException, IOException;


    /**
     * Set the turn manually by prividing a phase from {@link datastructures.Phase}
     *
     * @param gameId needs to be provided to identify the correct game
     * @param phase the game phase {@link datastructures.Phase}
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchPlayerException The turn also contains the Player that's why there is a check if that player exists
     * @throws IOException if a socket exception occurs
     * @throws CardAlreadyOwnedException
     * @throws NoSuchCardException
     */
    void setTurn(UUID gameId, Phase phase) throws GameNotFoundException, NoSuchPlayerException, IOException, CardAlreadyOwnedException, NoSuchCardException;

    /**
     * Switches turns based on the current turn and the Order: PLACE_UNITS -> ... -> USE_CARDS {@link datastructures.Phase}
     *
     * @param gameId needs to be provided to identify the correct game
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchPlayerException he turn also contains the Player that's why there is a check if that player exists
     * @throws NoSuchCardException
     * @throws CardAlreadyOwnedException
     * @throws IOException if a socket exception occurs
     */
    void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException;

    /**
     * Updates the Player-Graph map to update which countries owned by the player are connected.
     * Needs to be called after a country ownership is switched.
     *
     * @param gameId needs to be provided to identify the correct game
     * @param p the player whose graph needs to be updated
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchPlayerException if the player does not exist
     * @throws IOException if a socket exception occurs
     */
    void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException, IOException;

    /**
     * Checks if something needs to be done after a phase is over. (e.g. removing a player, checking a winning condition)
     *
     * @param gameId needs to be provided to identify the correct game
     * @param turn
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws IOException if a socket exception occurs
     * @throws NoSuchPlayerException if the player does not exist
     * @throws NoSuchCardException
     * @throws CardAlreadyOwnedException
     */
    void postPhaseCheck(UUID gameId, Turn turn) throws GameNotFoundException, IOException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;

    /**
     * Method to save the game
     * Games are saved in savedgames.dat server-side
     *
     * @param gameId needs to be provided to identify the correct game
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws IOException if a socket exception or file system exception occurs
     * @throws DuplicateGameIdException if a game with the same Id has already been saved
     */
    void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException;

    /**
     * Removes a game from saved games
     *
     * @param gameId needs to be provided to identify the correct game
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws IOException if a socket exception or file system exception occurs
     */
    void removeGame(UUID gameId) throws GameNotFoundException, IOException;

    /**
     * Loads all GameIds that are ready to be loaded
     *
     * @return List of Ids (Strings)
     * @throws IOException if a socket exception or file system exception occurs
     * @throws ClassNotFoundException
     */
    List<String> loadAvailableGameIds() throws IOException, ClassNotFoundException;
}