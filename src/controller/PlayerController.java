package controller;

import exceptions.InvalidPlayerNameException;
import exceptions.MaximumNumberOfPlayersReachedException;
import model.Game;
import model.Player;
import utils.Utils;

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
     * adds a player to a game.
     * @param game
     * @param name
     * @return
     * @throws MaximumNumberOfPlayersReachedException
     * @throws InvalidPlayerNameException
     */
    Player addPlayer(Game game, String name) throws MaximumNumberOfPlayersReachedException, InvalidPlayerNameException {
        if (game.getPlayers().size() >= 5) {
            throw new MaximumNumberOfPlayersReachedException("Maximum number of Players reached.");
        }
        if(Utils.stringContainsDelimitters(name)) {
            throw new InvalidPlayerNameException("No delimiter signs allowed for Player name.");
        }
        Player newPlayer = new Player(name);
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
            throw new MaximumNumberOfPlayersReachedException("There are too much players to assign missions for.");
        }
        for (int i = 0; i < game.getPlayers().size(); i++) {
            game.getPlayers().get(i).setMission(game.getMissions().get(i));
        }
    }
}
