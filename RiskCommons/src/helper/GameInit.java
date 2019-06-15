package helper;

import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class GameInit {

    private UUID gameId;
    private List<String> playerList;
    private List<Socket> sockets;

    public GameInit(UUID gameId, List<String> playerList, List<Socket> sockets) {
        this.gameId = gameId;
        this.playerList = playerList;
        this.sockets = sockets;
    }

    public UUID getGameId() {
        return gameId;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public List<Socket> getSockets() {
        return sockets;
    }
}
