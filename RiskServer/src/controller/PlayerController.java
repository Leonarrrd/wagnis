package controller;

import datastructures.Color;
import exceptions.InvalidPlayerNameException;
import exceptions.MaximumNumberOfPlayersReachedException;
import helper.Utils;
import model.Game;
import model.Player;

import java.util.UUID;

/**
 * Controller that handles the player mechanism.
 */
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

    /**
     * @see interfaces.IGameController#addPlayer(UUID, String, Color)
     */
    Player addPlayer(Game game, String name, Color color) throws MaximumNumberOfPlayersReachedException, InvalidPlayerNameException {
        if (game.getPlayers().size() >= 5) {
            throw new MaximumNumberOfPlayersReachedException(game.getPlayers().size());
        }
        if(Utils.stringContainsDelimitters(name)) {
            throw new InvalidPlayerNameException(name);
        }
        Player newPlayer = new Player(name, color);
        game.getPlayers().add(newPlayer);
        return newPlayer;

    }

    /**
     * Assigns missions to players
     * Missions are assigned randomly because the missions list is shuffled on creation
     * An assigned mission will be removed from the list
     *
     * @param game
     */
    void assignMissions(Game game) throws MaximumNumberOfPlayersReachedException {
        if(game.getPlayers().size() > 5) {
            throw new MaximumNumberOfPlayersReachedException(game.getPlayers().size());
        }
        for (int i = 0; i < game.getPlayers().size(); i++) {
            game.getPlayers().get(i).setMission(game.getMissions().get(i));
        }
    }
}
