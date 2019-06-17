package view.gui.sockets;

import datastructures.CardBonus;
import datastructures.Color;
import exceptions.*;
import interfaces.IGameController;
import model.*;
import view.gui.helper.GUIControl;
import view.gui.sockets.threads.LobbyWaitThread;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static helper.Events.*;
import static view.gui.util.UIConstants.SOCKET_PORT;

/**
 * Client-side implementation of the game logic
 * Implements IGameController
 * The corresponding GameController on the server also implements IGameController
 * A connection is made by sockets
 */
public class GameControllerFacade implements IGameController {

    private static IGameController instance;
    public Socket clientSocket;
    OutputStream os;
    InputStream is;
    ObjectInputStream ois;
    ObjectOutputStream oos;

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
            os = clientSocket.getOutputStream();
            is = clientSocket.getInputStream();
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void createGameRoom(UUID gameId, String hostPlayerName, Socket socket) throws IOException {


        oos.writeUTF(CREATE_GAME + "," + gameId.toString() + "," + hostPlayerName + "," + clientSocket.getInetAddress().toString());
        oos.flush();


        Thread waitThread = new LobbyWaitThread(ois);
        waitThread.start();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game getGameById(UUID id) throws IOException, ClassNotFoundException {

        oos.writeUTF(GET_GAME + "," + id.toString());
        oos.flush();

        return (Game) ois.readObject();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void initNewGame(UUID gameId) throws IOException {
        oos.writeUTF(START_GAME + "," + gameId.toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game loadGame(UUID gameId) throws IOException, ClassNotFoundException {
        oos.writeUTF(LOAD_GAME + "," + gameId.toString());

        oos.flush();
        return (Game) ois.readObject();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addPlayer(UUID gameId, String playerName, Color color) throws IOException {

        oos.writeUTF(PLAYER_JOIN + "," + gameId.toString() + "," + playerName);
        oos.flush();

        Thread waitThread = new LobbyWaitThread(ois);
        waitThread.start();

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCountry(UUID gameId, String countryAsString, Player player) throws IOException {
        oos.writeUTF(ADD_COUNTRY + "," + gameId.toString() + "," + countryAsString + "," + player.getName());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeUnits(UUID gameId, Country country, int units) throws IOException {
        oos.writeUTF(CHANGE_UNITS + "," + gameId.toString() + "," + country.getName() + "," + units);
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) {
        //TODO: not used
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void assignCountries(UUID gameId)  {
        throw new UnsupportedOperationException("This method does not need to be called on client side.");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void assignMissions(UUID gameId) {
        throw new UnsupportedOperationException("This method does not need to be called on client side.");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public int assignUnits(UUID gameId) throws IOException {
        oos.writeUTF(ASSIGN_UNITS + "," + gameId.toString());
        oos.flush();
        return 0;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCardToPlayer(UUID gameId, Player player) throws IOException {
        oos.writeUTF(ADD_CARD + "," + gameId.toString() + "," + player.getName());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCardsToDeck(UUID gameId, List<Card> cards) {
        throw new UnsupportedOperationException("This method does not need to be called on client side.");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards) {
        //MARK: I don't know what to do with this
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void awardUnits(UUID gameId, Player player) throws IOException {
        oos.writeUTF(AWARD_UNITS + "," + gameId.toString() + "," + player.getName());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws IOException {
        oos.writeUTF(CHANGE_UNITS_TO_PLACE + "," + gameId.toString() + "," + player.getName() + "," + unitChange);
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws IOException {
        oos.writeUTF(USE_CARDS + "," + gameId.toString() + "," + player.getName() + "," + infantryCards + "," + cavalryCards + "," + artilleryCards);
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws IOException, ClassNotFoundException {
        oos.writeUTF(GET_COUNTRIES_ATTACK_CAN_BE_LAUNCHED_FROM + "," + gameId.toString());
        oos.flush();

        return (Map<String, Country>) ois.readObject();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws IOException, ClassNotFoundException {
        oos.writeUTF(GET_COUNTRIES_WITH_MORE_THAN_ONE_UNIT + "," + gameId.toString() + "," + player.getName());
        oos.flush();

        return (Map<String, Country>) ois.readObject();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean hasCountryToMoveFrom(UUID gameId, Player player) throws IOException {
        oos.writeUTF(HAS_COUNTRY_TO_MOVE_FROM + "," + gameId.toString() + "," + player.getName());
        oos.flush();

        return ois.readBoolean();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean hasCountryToAttackFrom(UUID gameId, Player player) throws IOException {
        oos.writeUTF(HAS_COUNTRY_TO_ATTACK_FROM + "," + gameId.toString() + "," + player.getName());
        oos.flush();

        return ois.readBoolean();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws IOException, ClassNotFoundException {
        oos.writeUTF(GET_HOSTILE_NEIGHBORS + "," + gameId.toString() + "," + country.getName());
        oos.flush();

        return (Map<String, Country>) ois.readObject();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws IOException, ClassNotFoundException {
        oos.writeUTF(FIGHT +  "," + gameId.toString() + "," +attackingCountry.getName() + "," + defendingCountry.getName() + "," + attackingUnits + ","+ defendingUnits);
        oos.flush();

        return (AttackResult) ois.readObject();

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws IOException {
        oos.writeUTF(MOVE + "," + gameId.toString() + "," + srcCountry.getName() + "," + destCountry.getName() + "," + amount);
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean checkWinCondidtion(UUID gameId, Player player) throws IOException {
        oos.writeUTF(CHECK_WIN_CONDITION + "," + gameId.toString() + "," + player.getName());
        oos.flush();

        return ois.readBoolean();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void setTurn(UUID gameId) throws GameNotFoundException {
        //TODO: do we need this
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void switchTurns(UUID gameId) throws IOException {
        oos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void updatePlayerGraphMap(UUID gameId, Player p) throws IOException {
        oos.writeUTF(UPDATE_PLAYER_GRAPH_MAP + "," + gameId.toString() + "," + p.getName());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void postPhaseCheck(UUID gameId, Turn turn) throws IOException {
        oos.writeUTF(POST_PHASE_CHECK + "," + gameId.toString() + "," + turn.getPhase().toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public boolean isConnected(UUID gameId, Country srcCountry, Country destCountry) throws IOException {
        oos.writeUTF(IS_CONNECTED + "," + gameId.toString() + "," + srcCountry.getName() + "," + destCountry.getName());
        oos.flush();

        return ois.readBoolean();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void saveGame(UUID gameId) throws IOException {
        oos.writeUTF(SAVE_GAME + "," + gameId.toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void removeGame(UUID gameId) throws IOException {
        oos.writeUTF(REMOVE_GAME + "," + gameId.toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public List<String> loadAvailableGameIds() throws IOException, ClassNotFoundException {
        oos.writeUTF(LOAD_AVAILABLE_GAME_IDS);
        oos.flush();

        return (List<String>) ois.readObject();
    }
}
