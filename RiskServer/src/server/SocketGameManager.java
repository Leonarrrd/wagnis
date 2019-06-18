package server;

import exceptions.GameNotFoundException;
import helper.GameInit;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class SocketGameManager {

    private static SocketGameManager instance;
    private List<GameInit> gameInitList = new ArrayList();
    private List<String> sockets = new ArrayList();
    private Map<UUID, List<Socket>> gameIdSocketMap = new HashMap<>();
    private Map<Socket, ObjectOutputStream> socketObjectOutputStreamMap = new HashMap<>();
    private Map<Socket, ObjectInputStream> socketObjectInputStreamMap = new HashMap<>();


    private SocketGameManager() {
    }

    public static SocketGameManager getInstance() {
        if (instance == null) {
            instance = new SocketGameManager();
        }
        return instance;
    }

    public void createNewGame(UUID gameId, String hostPlayerName, Socket socket) {
        GameInit gameInit = new GameInit(gameId, new ArrayList(), new ArrayList<>());
        System.out.println(gameId);
        gameInit.getPlayerList().add(hostPlayerName);
        gameInit.getSockets().add(socket);
        gameInitList.add(gameInit);
    }

    public void addPlayer(UUID gameId, String playerName, Socket socket) throws GameNotFoundException {
        getGameInitById(gameId).getPlayerList().add(playerName);
        getGameInitById(gameId).getSockets().add(socket);
    }


    public GameInit getGameInitById(UUID gameId) throws GameNotFoundException {

        for (GameInit gi : gameInitList) {
            if (gameId.toString().equals(gi.getGameId().toString())) {
                return gi;
            }
        }

        throw new GameNotFoundException(gameId);
    }

    public List<GameInit> getGameInitList() {
        return gameInitList;
    }

    public List<String> getSockets() {
        return sockets;
    }

    public Map<UUID, List<Socket>> getGameIdSocketMap() {
        return gameIdSocketMap;
    }

    public Map<Socket, ObjectOutputStream> getSocketObjectOutputStreamMap() {
        return socketObjectOutputStreamMap;
    }

    public Map<Socket, ObjectInputStream> getSocketObjectInputStreamMap() {
        return socketObjectInputStreamMap;
    }


}
