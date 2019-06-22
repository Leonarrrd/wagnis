package controller;

import datastructures.Color;
import datastructures.Phase;
import interfaces.IGameController;
import datastructures.CardBonus;
import model.*;
import exceptions.*;
import persistence.FileReader;
import persistence.FileWriter;
import helper.GameInit;
import server.SocketGameManager;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Server-side implementation of the game logic
 * Implements IGameController
 * The corresponding GameController on the client also implements IGameController
 */
public class GameController implements IGameController {

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

    //FIXME: i did not quite understand the igamecontroller purpose, we will talk about this
    @Override
    public String getPlayerName(){
        return "something went wrong";
    }

    @Override
    public void createGameRoom(UUID gameId, String hostPlayerName, Socket socket) {
        SocketGameManager.getInstance().createNewGame(gameId, hostPlayerName, socket);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game getGameById(UUID id) throws GameNotFoundException {
        Game game = activeGames.get(id);
        if (game != null) {
            return game;
        } else {
            throw new GameNotFoundException(id);
        }
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void initNewGame(UUID gameId) throws IOException, InvalidFormattedDataException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException, CountriesAlreadyAssignedException, GameNotFoundException, NoSuchPlayerException {
        Map<String, Country> countries = null;
        List<Continent> continents = null;
        List<Mission> missions = null;
        List<Card> cards = null;
        List<Card> cardDeck = null;


        countries = FileReader.getInstance().loadCountries();
        continents = FileReader.getInstance().loadContinents(new ArrayList(countries.values()));
        missions = FileReader.getInstance().loadMissions(continents);
        cards = cdc.createCardDeck();
        cardDeck = (ArrayList) ((ArrayList) cards).clone();

        GameInit gameInit = SocketGameManager.getInstance().getGameInitById(gameId);

        Game game = new Game(gameInit.getGameId(), countries, continents, missions, cards, cardDeck);

        //TODO: Farben könnte man auch noch schöner zuweisen.
        for (int i = 0; i < gameInit.getPlayerList().size(); i++) {
            Color color = Color.values()[i];
            pc.addPlayer(game, gameInit.getPlayerList().get(i), color);

        }

        activeGames.put(game.getId(), game);


        pc.assignMissions(game);
        wc.assignCountries(game);
        tc.setTurn(game);


        System.out.println(game.toString());

    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
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
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addPlayer(UUID gameId, String playerName, Color color) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException {
        Game game = getGameById(gameId);
        pc.addPlayer(game, playerName, color);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException {
        Game game = getGameById(gameId);
        wc.addCountry(game, countryAsString, player);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        wc.changeUnits(game, country, units);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        wc.changeFrozenUnits(game, country, frozenUnits);
    }


    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException {
        Game game = getGameById(gameId);
        wc.assignCountries(game);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException {
        Game game = getGameById(gameId);
        pc.assignMissions(game);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException {
        Game game = getGameById(gameId);
        return wc.assignUnits(game);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {
        Game game = getGameById(gameId);
        cdc.addCardToPlayer(game, player);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCardsToDeck(UUID gameId, List<Card> cards) throws GameNotFoundException {
        Game game = getGameById(gameId);
        cdc.addCardsToDeck(game, cards);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards) {
        return lc.getTradeBonusType(infantryCards, cavalryCards, artilleryCards);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        lc.awardUnits(game, player);
    }

    @Override
    public void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        lc.changeUnits(game, player, unitChange);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        lc.useCards(game, player, infantryCards, cavalryCards, artilleryCards);
    }


    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return wc.getCountriesAttackCanBeLaunchedFrom(game, player);
    }


    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        return wc.getCountriesWithMoreThanOneUnit(game, player);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean hasCountryToMoveFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return lc.hasCountryToMoveFrom(game, player);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean hasCountryToMoveTo(UUID gameId, Country country) throws GameNotFoundException {
        Game game = getGameById(gameId);
        return lc.hasCountryToMoveTo(game, country);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean hasCountryToAttackFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return lc.hasCountryToAttackFrom(game, player);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return wc.getHostileNeighbors(game, country);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException {
        Game game = getGameById(gameId);
        return lc.fight(game, attackingCountry, defendingCountry, attackingUnits, defendingUnits);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotAdjacentException {
        Game game = getGameById(gameId);
        wc.moveUnits(game, srcCountry, destCountry, amount);
    }


    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean checkWinCondidtion(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, IOException {
        Game game = getGameById(gameId);
        return lc.checkWinCondition(game, player);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void setTurn(UUID gameId, Phase phase) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        game.getTurn().setPhase(phase);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException {
        Game game = getGameById(gameId);
        tc.switchTurns(game);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException {
        Game game = getGameById(gameId);
        grc.updatePlayerGraphMap(game, p);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void postPhaseCheck(UUID gameId, Turn turn) throws GameNotFoundException, IOException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {
        Game game = getGameById(gameId);
        lc.postPhaseCheck(game, turn);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean isConnected(UUID gameId, Country srcCountry, Country destCountry) {
        return wc.isConnected(gameId, srcCountry, destCountry);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException {
        Game game = getGameById(gameId);
        FileWriter.getInstance().saveGame(game);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public void removeGame(UUID gameId) throws GameNotFoundException, IOException {
        Game game = getGameById(gameId);
        FileWriter.getInstance().removeGame(game);
    }

    /**
     * Server-side implementation
     * {@inheritDoc }
     */
    @Override
    public List<String> loadAvailableGameIds() throws IOException {
        return FileReader.getInstance().loadAvailableGameIds();
    }


}