package view.gui.sockets;

import datastructures.Color;
import datastructures.Phase;
import exceptions.*;
import interfaces.IGameController;
import model.*;
import view.gui.sockets.threads.ClientIOThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
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
    private static GameControllerFacade instance;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Game game;
    private String playerName;

    private GameControllerFacade() {
        initSocket();
    }

    public static GameControllerFacade getInstance() {
        if (instance == null) {
            instance = new GameControllerFacade();
        }
        return instance;
    }


    private void initSocket() {
        try {
            clientSocket = new Socket("localhost", SOCKET_PORT);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());

            Thread clientIOThread = new ClientIOThread(ois, oos);
            clientIOThread.start();

            GameLobbyManager.getInstance().setOos(oos);

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
        this.playerName = hostPlayerName;
        oos.writeUTF(CREATE_GAME + "," + gameId.toString() + "," + hostPlayerName + "," + clientSocket.getInetAddress().toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game getGameById(UUID id) throws GameNotFoundException, IOException, ClassNotFoundException {

        synchronized (game) {
            return game;
        }
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void initNewGame(UUID gameId) throws IOException, InvalidFormattedDataException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException, CountriesAlreadyAssignedException, GameNotFoundException, NoSuchPlayerException {
        oos.writeUTF(START_GAME + "," + gameId.toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException, ClassNotFoundException {
        oos.writeUTF(LOAD_GAME + "," + gameId.toString());
        oos.flush();

        //Game is read in ClientIOThread to support multiple sockets getting the game, not just the caller
        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addPlayer(UUID gameId, String playerName, Color color) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException, IOException {
        this.playerName = playerName;
        oos.writeUTF(PLAYER_JOIN + "," + gameId.toString() + "," + playerName);
        oos.flush();

    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException, IOException {
        oos.writeUTF(CHANGE_UNITS + "," + gameId.toString() + "," + country.getName() + "," + units);
        oos.flush();
    }


    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException {
        throw new UnsupportedOperationException("Only supported Serverside");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException, IOException {
        throw new UnsupportedOperationException("Only supported Serverside");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException, IOException {
        oos.writeUTF(USE_CARDS + "," + gameId.toString() + "," + player.getName() + "," + infantryCards + "," + cavalryCards + "," + artilleryCards);
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void initAttack(UUID gameId, String attackingCountry, String defendingCountry, int units) throws GameNotFoundException, NoSuchCountryException, IOException {
        oos.writeUTF(INIT_ATTACK + "," + gameId.toString() + "," + attackingCountry + "," + defendingCountry + "," + units);
        oos.flush();


    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException, IOException, ClassNotFoundException {
        oos.writeUTF(FIGHT + "," + gameId.toString() + "," + attackingCountry.getName() + "," + defendingCountry.getName() + "," + attackingUnits + "," + defendingUnits);
        oos.flush();

        return null;
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount, boolean trail) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotConnectedException, IOException {

        if (trail) {
            oos.writeUTF(MOVE + "," + gameId.toString() + "," + srcCountry.getName() + "," + destCountry.getName() + "," + amount + ",trail");
        } else {
            oos.writeUTF(MOVE + "," + gameId.toString() + "," + srcCountry.getName() + "," + destCountry.getName() + "," + amount + ",notrail");
        }
        oos.flush();

    }

      /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void setTurn(UUID gameId, Phase phase) throws GameNotFoundException, NoSuchPlayerException, IOException {
        oos.writeUTF(SET_TURN + "," + gameId.toString() + "," + phase.toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException {
        oos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
        oos.flush();
    }

    public void switchTurns(UUID gameId, boolean notifyAll) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException {
        String notifyAllFlag = notifyAll ? "doNotifyAll" : "dontNotifyAll";
        oos.writeUTF(SWITCH_TURNS + "," + gameId.toString() + "," + notifyAllFlag);
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException, IOException {
        throw new UnsupportedOperationException("Only supported Serverside");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void postPhaseCheck(UUID gameId, Turn turn) throws GameNotFoundException, IOException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {
        throw new UnsupportedOperationException("Only supported Serverside");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException {
        oos.writeUTF(SAVE_GAME + "," + gameId.toString());
        oos.flush();
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public void removeGame(UUID gameId) throws GameNotFoundException, IOException {
        throw new UnsupportedOperationException("Only supported Serverside");
    }

    /**
     * Client-side implementation
     * {@inheritDoc }
     */
    @Override
    public List<String> loadAvailableGameIds() throws IOException, ClassNotFoundException {
        oos.writeUTF(LOAD_AVAILABLE_GAME_IDS);
        oos.flush();
        return null;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getPlayerName() {
        return playerName;
    }
}