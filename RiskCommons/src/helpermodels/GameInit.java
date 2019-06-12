package helpermodels;

import model.Player;

import java.util.List;
import java.util.UUID;

public class GameInit {

    private UUID gameId;
    private List<String> playerList;

    public GameInit(UUID gameId, List<String> playerList) {
        this.gameId = gameId;
        this.playerList = playerList;
    }

    public UUID getGameId() {
        return gameId;
    }

    public List<String> getPlayerList() {
        return playerList;
    }
}
