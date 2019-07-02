package server;

import exceptions.GameNotFoundException;
import exceptions.InvalidPlayerNameException;
import exceptions.MaximumNumberOfPlayersReachedException;
import helper.GameInit;
import helper.Utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class SocketGameManager {

    private static SocketGameManager instance;
    private List<GameInit> gameInitList = new ArrayList();
    private Map<UUID, List<Socket>> gameIdSocketMap = new HashMap<>();
    private Map<Socket, ObjectOutputStream> socketObjectOutputStreamMap = new HashMap<>();
    private Map<Socket, ObjectInputStream> socketObjectInputStreamMap = new HashMap<>();
    private Map<Socket, String> socketPlayerNameMap = new HashMap<>();


    private SocketGameManager() {
    }

    public static SocketGameManager getInstance() {
        if (instance == null) {
            instance = new SocketGameManager();
        }
        return instance;
    }

    /**
     * creates a new game Lobby to collect players to start the game
     *
     * @param gameId         The new game Id (Caller might pass a newly created UUID)
     * @param hostPlayerName The name of the player that hosts the game
     * @param socket         The socket for the host player
     */
    public void createNewGame(UUID gameId, String hostPlayerName, Socket socket) throws InvalidPlayerNameException{

        if(Utils.stringContainsDelimitters(hostPlayerName)) {
            throw new InvalidPlayerNameException(hostPlayerName);
        }

        GameInit gameInit = new GameInit(gameId, new ArrayList(), new ArrayList<>());
        System.out.println(gameId);
        gameInit.getPlayerList().add(hostPlayerName);
        gameInit.getSockets().add(socket);
        socketPlayerNameMap.put(socket, hostPlayerName);
        gameInitList.add(gameInit);
    }

//    /**
//     * creates a new game Lobby to collect players to start the game
//     *
//     * @param gameId         The new game Id (Caller might pass a newly created UUID)
//     * @param hostPlayerName The name of the player that hosts the game
//     * @param socket         The socket for the host player
//     */
//    public void createLoadedGame(UUID gameId, String hostPlayerName, Socket socket) throws InvalidPlayerNameException{
//
//        if(Utils.stringContainsDelimitters(hostPlayerName)) {
//            throw new InvalidPlayerNameException(hostPlayerName);
//        }
//
//        GameInit gameInit = new GameInit(gameId, new ArrayList(), new ArrayList<>());
//        System.out.println(gameId);
//        gameInit.getPlayerList().add(hostPlayerName);
//        gameInit.getSockets().add(socket);
//        socketPlayerNameMap.put(hostPlayerName, socket);
//        gameInitList.add(gameInit);
//    }

    /**
     * adds a player to an existing GameInit (Lobby) by providing the game Id for the specific lobby
     *
     * @param gameId     The game Id for the exisiting Lobby
     * @param playerName The name of the player that is being joined
     * @param socket     the socket for the player that is being joined
     * @throws GameNotFoundException
     */
    public void addPlayer(UUID gameId, String playerName, Socket socket) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException {

        GameInit gameInit = getGameInitById(gameId);

        if(gameInit.getPlayerList().size() >= 5) {
            throw new MaximumNumberOfPlayersReachedException(gameInit.getPlayerList().size());
        }

        if(Utils.stringContainsDelimitters(playerName)) {
            throw new InvalidPlayerNameException(playerName);
        }

        gameInit.getPlayerList().add(playerName);
        gameInit.getSockets().add(socket);

        socketPlayerNameMap.put(socket, playerName);
    }


    /**
     * Returns the GameInit (Lobby) Object for
     *
     * @param gameId
     * @return
     * @throws GameNotFoundException
     */
    public GameInit getGameInitById(UUID gameId) throws GameNotFoundException {

        for (GameInit gi : gameInitList) {
            if (gameId.toString().equals(gi.getGameId().toString())) {
                return gi;
            }
        }

        throw new GameNotFoundException(gameId);
    }

    public void removeGameInit(UUID gameId) throws GameNotFoundException {
        gameInitList.remove(getGameInitById(gameId));
    }

    public Map<UUID, List<Socket>> getGameIdSocketMap() {
        return gameIdSocketMap;
    }

    public List<GameInit> getGameInitList() {
        return gameInitList;
    }

    public Map<Socket, ObjectOutputStream> getSocketObjectOutputStreamMap() {
        return socketObjectOutputStreamMap;
    }

    public Map<Socket, ObjectInputStream> getSocketObjectInputStreamMap() {
        return socketObjectInputStreamMap;
    }

    public Map<Socket, String> getSocketPlayerNameMap() {
        return socketPlayerNameMap;
    }
}
