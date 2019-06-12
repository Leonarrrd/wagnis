package serverhelper;


import exceptions.GameNotFoundException;
import helpermodels.GameInit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InitHelper {
    public static List<GameInit> gameInitList = new ArrayList();

    public static void createNewGame(UUID gameId, String hostPlayerName) {
        GameInit gameInit = new GameInit(gameId, new ArrayList());
        System.out.println(gameId);
        gameInit.getPlayerList().add(hostPlayerName);
        gameInitList.add(gameInit);
    }

    public static void addPlayer(UUID gameId, String playerName) throws GameNotFoundException {
        getGameInitById(gameId).getPlayerList().add(playerName);
    }


    public static GameInit getGameInitById(UUID gameId) throws GameNotFoundException {

        for(GameInit gi : gameInitList) {
            if(gameId.toString().equals(gi.getGameId().toString())) {
                return gi;
            }
        }

        throw new GameNotFoundException(gameId);
    }
}
