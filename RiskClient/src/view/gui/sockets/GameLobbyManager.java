package view.gui.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameLobbyManager {
    private static GameLobbyManager instance;
    private ObjectOutputStream oos;

    private GameLobbyManager() {
    }

    public static GameLobbyManager getInstance() {
        if (instance == null) {
            instance = new GameLobbyManager();
        }
        return instance;
    }

    public void setOos(ObjectOutputStream oos){
        this.oos = oos;
    }

    public void checkGameType(String gameId) throws IOException {
        oos.writeUTF("CHECK_GAME_TYPE" + "," + gameId);
        oos.flush();
    }
}
