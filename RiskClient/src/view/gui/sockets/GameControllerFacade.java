package view.gui.sockets;

import datastructures.CardBonus;
import exceptions.*;
import interfaces.IGameController;
import model.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static view.gui.util.UIConstants.SOCKET_PORT;

/**
 * Client-side implementation of the game logic
 * Implements IGameController
 * The corresponding GameController on the server also implements IGameController
 * A connection is made by sockets
 */
public class GameControllerFacade implements IGameController {

    private static IGameController instance;
    private Socket clientSocket;
    private PrintWriter printWriter;

    private GameControllerFacade() {
        initSocket();
    }

    public static IGameController getInstance() {
        if (instance == null) {
            instance = new GameControllerFacade();
        }
        return instance;
    }

    public static void main(String... args) {
        getInstance();
    }

    private void initSocket() {
        try {
            clientSocket = new Socket("localhost", SOCKET_PORT);
            printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            printWriter.println("Hello from socket");
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


        printWriter.println("Socket " + clientSocket.getLocalPort() + " connected!");
        printWriter.flush();
        printWriter.println("Hallo new");
        printWriter.flush();
    }


    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game getGameById(UUID id) throws GameNotFoundException {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game initNewGame() throws IOException, InvalidFormattedDataException {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addPlayer(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException {
        return 0;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCardsToDeck(UUID gameId, List<Card> cards) {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards) {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean hasCountryToMoveFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        return false;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean hasCountryToAttackFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        return false;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws GameNotFoundException, NoSuchCountryException {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException {
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotAdjacentException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean checkWinCondidtion(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, IOException {
        return false;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void setTurn(UUID gameId) throws GameNotFoundException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void postPhaseCheck(UUID gameId, Turn turn) throws GameNotFoundException, IOException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean isConnected(Country srcCountry, Country destCountry) {
        return false;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void removeGame(UUID gameId) throws GameNotFoundException, IOException {

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public List<String> loadAvailableGameIds() throws IOException {
        return null;
    }
}
