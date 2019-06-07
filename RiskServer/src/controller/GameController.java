package controller;

import model.*;
import exceptions.*;
import persistence.FileReader;

import java.io.IOException;
import java.util.*;

/*
 * Class to handle game logic
 */
public class GameController {

    private Map<UUID, Game> activeGames = new HashMap<>();

    private static GameController instance;
    private LogicController lc = LogicController.getInstance();
    private PlayerController pc = PlayerController.getInstance();
    private WorldController wc = WorldController.getInstance();
    private TurnController tc = TurnController.getInstance();
    private CardDeckController cdc = CardDeckController.getInstance();
    private GraphController grc = GraphController.getInstance();

    private GameController() {
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    /**
     * gets the game by providing a game id.
     * @param id
     * @return the main game object
     * @throws GameNotFoundException if the game does not exist
     */
    public Game getGameById(UUID id) throws GameNotFoundException {
        Game game = activeGames.get(id);
        if (game != null) {
            return game;
        } else {
            throw new GameNotFoundException(" Game with id " + id.toString() + " could not be found.");
        }
    }

    /**
     * inits a new game
     * @return
     * @throws IOException
     * @throws InvalidFormattedDataException
     * @throws GameNotFoundException
     */
    public Game initNewGame() throws IOException, InvalidFormattedDataException {
        Map<String, Country> countries = null;
        List<Continent> continents = null;
        List<Mission> missions = null;
        List<Card> cards = null;
        List<Card> cardDeck = null;


        countries = FileReader.getInstance().loadCountries();
        continents = FileReader.getInstance().loadContinents(new ArrayList(countries.values()));
        missions = FileReader.getInstance().loadMissions(continents);
        cards = cdc.createCardDeck();
        cardDeck = (ArrayList) ((ArrayList)cards).clone();

        Game game = new Game(UUID.randomUUID(), countries, continents, missions, cards, cardDeck);
        activeGames.put(game.getId(), game);

        return game;
    }

    /**
     * load an exisitng game from saved games
     * @param gameId
     * @return
     * @throws IOException
     * @throws GameNotFoundException
     * @throws InvalidFormattedDataException
     */
    public Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException {
        List<Country> loadedCountries = new ArrayList<>(FileReader.getInstance().loadCountries().values());
        List<Continent> loadedContinents = new ArrayList<>(FileReader.getInstance().loadContinents(loadedCountries));
        List<Mission> loadedMissions = new ArrayList<>(FileReader.getInstance().loadMissions(loadedContinents));
        List<Card> loadedCards = cdc.createCardDeck();


        Game game = FileReader.getInstance().loadGame(gameId, loadedCountries, loadedContinents, loadedMissions, loadedCards);
        activeGames.put(game.getId(), game);
        return game;
    }


    /**
     * adds a player to a game
     *
     * @param gameId
     * @param playerName
     * @throws GameNotFoundException
     */
    public void addPlayer(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException {
        Game game = getGameById(gameId);
        pc.addPlayer(game, playerName);
    }

    /**
     * TODO: kann die raus? add description
     * @param gameId
     * @param countryAsString
     * @param player
     * @throws GameNotFoundException
     * @throws CountryAlreadyOccupiedException
     */
    public void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException {
        Game game = getGameById(gameId);
        wc.addCountry(game, countryAsString, player);
    }

    /**
     * Add units to the specified country
     *
     * @param country
     * @param units   units to add
     */
    public void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        wc.changeUnits(game, country, units);
    }

    /**
     * TODO: add description
     * @param gameId
     * @param country
     * @param frozenUnits
     * @throws GameNotFoundException
     */
    public void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        wc.changeFrozenUnits(game, country, frozenUnits);
    }


    /**
     * Assigns all countries to players randomly
     * Only used in simple game mode, for normal game mode, we let people choose their own country, see UI
     *
     * @param gameId
     * @throws GameNotFoundException
     */
    public void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException {
        Game game = getGameById(gameId);
        wc.assignCountries(game);
    }

    /**
     * Assigns missions to players
     * Missions are assigned randomly because the missions list is shuffled on creation
     * An assigned mission will be removed from the list
     * @param gameId
     * @throws GameNotFoundException
     */
    public void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException {
        Game game = getGameById(gameId);
        pc.assignMissions(game);
    }

    /**
     * Assigns a number of starting units to players depending on the number of players
     * Actually, we could put this in the Game class
     *
     * @return the amount of units assigned
     */
    public int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException {
        Game game = getGameById(gameId);
        return wc.assignUnits(game);
    }

    public void addCard(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {
        Game game = getGameById(gameId);
        cdc.addCard(game, player);
    }

    /**
     * Calculates the amount of units the player is eligible to get for this turn
     * Get units for: default, total number of countries, full continents, Cards played
     *
     * @return number of units the player can place this turn
     */
    public void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        lc.awardUnits(game, player);
    }

    public void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException{
        Game game = getGameById(gameId);
        lc.changeUnits(game, player, unitChange);
    }

    /**
     * OVERSIMPLIFIED AT THE MOMENT
     * Removes the cards the player wants to use from his hand
     * Calculates and returns the number of units the player is awarded for the value of his cards
     *
     * @param player
     * @param
     * @return
     */
    public void useCards(UUID gameId, Player player, int oneStarCards, int twoStarCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        lc.useCards(game, player, oneStarCards, twoStarCards);
    }


    /**
     * Returns a map so printing keys in the interface can be do more easily
     * Looks at all the countries a player owns and creates a map of those that have more than one unit on them
     * Used to determine eligible source attack and move countries
     *
     * @param player
     * @return map of countries with more than one unit
     */
    public Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return wc.getCountriesAttackCanBeLaunchedFrom(game, player);
    }


    /**
     * @param player
     * @return countries with more than one unit
     */
    public Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        return wc.getCountriesWithMoreThanOneUnit(game, player);
    }

    /**
     * Returns true if player has a country that has: a) more than one unit, b) is connected to at least one friendly country
     *
     * @param player
     * @return
     */
    public boolean hasCountryToMoveFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return lc.hasCountryToMoveFrom(game, player);
    }

    /**
     * Returns true if player has a country that has: a) more than one unit, b) at least one hostile neighbor
     *
     * @param player
     * @return
     */
    public boolean hasCountryToAttackFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return lc.hasCountryToAttackFrom(game, player);
    }

    /**
     * Looks at all neighbors the country has and creates a map of the ones that are not occupied by the player occupying it
     * Used to determine eligible attack destinations
     *
     * @param country
     * @return
     */
    public Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return wc.getHostileNeighbors(game, country);
    }

    /**
     * Carries out one round of an attack
     * Rolls the dices and deducts units from the countries depending on the roll's outcome
     * The method does NOT handle invalid parameters. The instance that calls this function must ensure the parameters are valid
     * The method is rather long and might be rewritten in a more elegant way
     * Probably better to split this method in 2-3 smaller methods, with dice rolls, unit deducts, and checking for a winner separate
     *
     * @param attackingCountry
     * @param defendingCountry
     * @param attackingUnits
     * @param defendingUnits
     * @return null if both sides still have enough units to fight, the winner of the war otherwise
     */
    public AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return lc.fight(game, attackingCountry, defendingCountry, attackingUnits, defendingUnits);
    }

    /**
     * Method to move units from one country to another
     *
     * @param srcCountry
     * @param destCountry
     * @param amount
     */
    public void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotAdjacentException {
        Game game = getGameById(gameId);
        wc.moveUnits(game, srcCountry, destCountry, amount);
    }


    /**
     *
     * @param player
     * @return
     */
    public boolean checkWinCondidtion(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, IOException {
        Game game = getGameById(gameId);
        return lc.checkWinCondition(game, player);
    }

    /**
     * inits turn object at the start of the game
     *
     * @param gameId
     */
    public void setTurn(UUID gameId) throws GameNotFoundException {
        Game game = getGameById(gameId);
        tc.setTurn(game);
    }

    /**
     * switch turns (phase or phase and player)
     *
     */
    public void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {
        Game game = getGameById(gameId);
        tc.switchTurns(game);
    }

    /**
     * updates the player graph
     * @param gameId
     * @param p
     */
    public void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        grc.updatePlayerGraphMap(game, p);
    }

    public void postTurnCheck(UUID gameId, Player player) throws GameNotFoundException, IOException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        lc.postTurnCheck(game, player);
    }

    /**
     * method that checks if countries are connected
     * @param srcCountry
     * @param destCountry
     * @return
     */
    public boolean isConnected(Country srcCountry, Country destCountry) {
        return wc.isConnected(srcCountry, destCountry);
    }

    public void testMethod() {
        System.out.println("test");
    }


}