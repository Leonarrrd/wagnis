package view.cui;

import exceptions.GameNotFoundException;
import exceptions.NoSuchPlayerException;
import model.Game;
import model.Player;

public class TurnCUI extends AbstractCUI {


    /**
     * Main gameloop turn method
     * checks for winning condition and exectures the turn with all its methods for the player in the right sequence
     */
    @Override
    public void run() {
        Game game = null;
        try {
            game = gc.getGameById(gameId);
        } catch (GameNotFoundException e) {
            e.printStackTrace();
        }

        initGameLoop(game);


        gameResult(game.getTurn().getPlayer());


    }

    private void initGameLoop(Game game) {
        boolean gameWon = false;

        while (!gameWon) {
            Player player = game.getTurn().getPlayer();
            System.out.println(game.getTurn().getPlayer() + "'s turn!");

            try {
                switch (game.getTurn().getPhase()) {
                    case ATTACK:
                        new AttackCUI().run();
                        gc.switchTurns(gameId);
                        break;
                    case PLACE_UNITS:
                        new PlaceUnitsCUI().run();
                        gc.switchTurns(gameId);
                        break;
                    case MOVE:
                        new MoveCUI().run();
                        gc.switchTurns(gameId);
                        break;

                }
            } catch (GameNotFoundException e) {
                e.printStackTrace();
            }

            // FIXME: Wrong place, maybe put it at the end of attack Method? (Or right after player has conquered a country)
            boolean winCondition = false;
            try {
                winCondition = gc.checkWinCondidtion(gameId, player);
            } catch (GameNotFoundException | NoSuchPlayerException e) {
                e.printStackTrace();
            }
            if (winCondition) {
                gameWon = true;
                break;
            }

            // FIXME: Wrong place, maybe put it at the end of move method?
            if (player.hasConqueredCountry()) {
                try {
                    System.out.println(player + " is awarded a card");
                    try {
                        gc.addCard(gameId, player);
                    } catch (NoSuchPlayerException e) {
                        e.printStackTrace();
                    }
                    player.setHasConqueredCountry(false);
                } catch (GameNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void gameResult(Player player) {
        System.out.println(player + " has won the Game!");
        System.out.println("Exiting...");
        System.exit(0);
    }
}
