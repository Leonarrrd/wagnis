//MARK: CUI is abandoned


//package view.cui;
//
//import exceptions.CardAlreadyOwnedException;
//import exceptions.GameNotFoundException;
//import exceptions.NoSuchCardException;
//import exceptions.NoSuchPlayerException;
//import model.Game;
//import model.Player;
//
//import java.io.IOException;
//
//public class TurnCUI extends AbstractCUI {
//
//
//    /**
//     * view.cui.Main gameloop turn method
//     * checks for winning condition and exectures the turn with all its methods for the player in the right sequence
//     */
//    @Override
//    public void run() {
//        Game game = null;
//        try {
//            game = gc.getGameById(gameId);
//        } catch (GameNotFoundException e) {
//            e.printStackTrace();
//        }
//        initGameLoop(game);
//    }
//
//    private void initGameLoop(Game game) {
//        boolean gameWon = false;
//
//        while (!gameWon) {
//            Player player = game.getTurn().getThisPlayer();
//            System.out.println(game.getTurn().getThisPlayer() + "'s turn!");
//
//            try {
//                switch (game.getTurn().getPhase()) {
//                    case ATTACK:
//                        new AttackCUI().run();
//                        gc.switchTurns(gameId);
//                        break;
//                    case PLACE_UNITS:
//                        new PlaceUnitsCUI().run();
//                        gc.switchTurns(gameId);
//                        break;
//                    case MOVE:
//                        new MoveCUI().run();
//                        gc.switchTurns(gameId);
//                        break;
//                }
//                postTurnCheck(game, gameWon, player);
//            } catch (GameNotFoundException | NoSuchCardException | IOException | NoSuchPlayerException | CardAlreadyOwnedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //FIXME: Needs to be done in the controller asap
//    private void postTurnCheck(Game game, boolean gameWon, Player player) {
//
//        boolean winCondition = false;
//        try {
//            winCondition = gc.checkWinCondidtion(gameId, player);
//        } catch (GameNotFoundException | NoSuchPlayerException | IOException e) {
//            e.printStackTrace();
//        }
//        if (winCondition) {
//            gameResult(game.getTurn().getThisPlayer());
//            return;
//        }
//
//        if (player.hasConqueredCountry()) {
//            try {
//                System.out.println(player + " is awarded a card");
//                try {
//                    gc.addCardToPlayer(gameId, player);
//                } catch (NoSuchPlayerException | NoSuchCardException | CardAlreadyOwnedException e) {
//                    e.printStackTrace();
//                }
//                player.setHasConqueredCountry(false);
//            } catch (GameNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    private void gameResult(Player player) {
//        System.out.println(player + " has won the Game!");
//        System.out.println("Exiting...");
//        System.exit(0);
//    }
//}
