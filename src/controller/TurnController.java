package controller;

import datastructures.Phase;
import exceptions.CardAlreadyOwnedException;
import exceptions.GameNotFoundException;
import exceptions.NoSuchCardException;
import exceptions.NoSuchPlayerException;
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
        // MARK ***************************** BULLSHIT **********************************
        //    the method call should be refactored to somewhere else
        try {
            GameController.getInstance().awardUnits(game.getId(), game.getPlayers().get(0));
        } catch (GameNotFoundException | NoSuchPlayerException e) {
            e.printStackTrace();
        }
        // MARK ***************************** BULLSHIT **********************************
    }

   /**
     * switch turns (phase or phase and player)
     *
     */
    void switchTurns(Game game) throws NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {

        Turn turn = game.getTurn();

        switch (game.getTurn().getPhase()) {
            case PLACE_UNITS:
                turn.setPhase(Phase.ATTACK);
                break;
            case ATTACK:
                turn.setPhase(Phase.TRAIL_UNITS);
                break;
            case TRAIL_UNITS:
                turn.setPhase(Phase.PERFORM_ANTOHER_ATTACK);
                break;
            case PERFORM_ANTOHER_ATTACK:
                turn.setPhase(Phase.MOVE);
                break;
            case MOVE:
                turn.setPhase(Phase.PERFORM_ANOTHER_MOVE);
                break;
            case PERFORM_ANOTHER_MOVE:
                turn.setPhase(Phase.PLACE_UNITS);
                CardDeckController.getInstance().addCard(game, turn.getPlayer());
                turn.setPlayer(getNextPlayer(game, turn.getPlayer()));
                // MARK ***************************** BULLSHIT **********************************
                //        the method call should be refactored to somewhere else
                try {
                    GameController.getInstance().awardUnits(game.getId(), turn.getPlayer());
                } catch (GameNotFoundException e) {
                    e.printStackTrace();
                }
                // MARK ***************************** BULLSHIT **********************************

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


