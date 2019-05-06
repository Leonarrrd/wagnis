package controller;

import datastructures.Phase;
import model.Game;
import model.Player;
import model.Turn;

public class TurnController {

    private static TurnController instance;

    private TurnController() {
    }

    static TurnController getInstance() {
        if (instance == null) {
            instance = new TurnController();
        }
        return instance;
    }


    /**
     * inits turn object at the start of the game
     *
     */
    public void setTurn(Game game) {
        game.setTurn(new Turn(game.getPlayers().get(0), Phase.PLACE_UNITS));

    }

   /**
     * switch turns (phase or phase and player)
     *
     */
    void switchTurns(Game game) {

        Turn turn = game.getTurn();

        switch (game.getTurn().getPhase()) {
            case PLACE_UNITS:
                turn.setPhase(Phase.ATTACK);
                break;
            case ATTACK:
                turn.setPhase(Phase.MOVE);
                break;
            case MOVE:
                turn.setPhase(Phase.PLACE_UNITS);
                turn.setPlayer(getNextPlayer(game, turn.getPlayer()));
                break;
            default:
                break;
        }
    }

    /**
     * Gets the player that has the turn after ther current player
     * @param game
     * @param player
     * @return player that has the next turn
     */
    private Player getNextPlayer(Game game, Player player) {
        int index = game.getPlayers().indexOf(player);
        int newIndex = 0;
        if (index != game.getPlayers().size() - 1) {
            newIndex = index + 1;
        }
        return game.getPlayers().get(newIndex);
    }

}


