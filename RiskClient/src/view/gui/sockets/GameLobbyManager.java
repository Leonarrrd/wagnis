package view.gui.sockets;

import exceptions.GameNotFoundException;
import exceptions.InvalidFormattedDataException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import static helper.Events.START_LOADED_GAME;

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

    /**
     * Starts a loaded game. Needs another implementation than starting a new game.
     *
     * @param gameId The game Id of the game that should be started.
     * @throws IOException if the game could not be read or a socket exception occurs.
     * @throws GameNotFoundException if the game with the provided Id could not be found.
     * @throws InvalidFormattedDataException if the file that the game is saved in is corrupted.
     * @throws ClassNotFoundException
     */
    public void startLoadedGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException, ClassNotFoundException {
        oos.writeUTF(START_LOADED_GAME + "," + gameId.toString());
        oos.flush();
    }
}
