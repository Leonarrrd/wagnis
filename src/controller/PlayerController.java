package controller;

import exceptions.MaximumNumberOfPlayersReachedException;
import model.Game;
import model.Player;

public class PlayerController {

    private static PlayerController instance;

    private PlayerController() {
    }

    static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }

    Player addPlayer(Game game, String name) throws MaximumNumberOfPlayersReachedException {
        if (game.getPlayers().size() >= 5) {
            throw new MaximumNumberOfPlayersReachedException("Maximum number of Players reached.");
        } else {
            Player newPlayer = new Player(name);
            game.getPlayers().add(newPlayer);
            return newPlayer;
        }
    }

}
