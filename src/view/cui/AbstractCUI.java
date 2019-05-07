package view.cui;

import controller.GameController;
import exceptions.DuplicateGameIdException;
import exceptions.GameNotFoundException;
import model.Game;
import persistence.FileWriter;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public abstract class AbstractCUI {

    protected GameController gc = GameController.getInstance();
    protected Scanner reader = new Scanner(System.in);
    protected static UUID gameId;


    public abstract void run();

    /**
     * if the input from the call is not a valid input for a player action
     * this method checks if it's a special input (like printing the game state with "state")
     * else the input is invalid and an error message is printed.
     *
     * @param input
     */
    protected void checkForSpecialInput(String input, String message) {
        Game game = null;
        try {
            game = gc.getGameById(gameId);
        } catch (GameNotFoundException e) {
            e.printStackTrace();
        }
        switch (input) {
            case "state":
                System.out.println(game.toString()+ "\n");
                break;
            case "quit":
                System.exit(0);
                break;
            case "save":
                try {
                    FileWriter.getInstance().saveGame(game);
                } catch (IOException | GameNotFoundException | DuplicateGameIdException e) {
                    e.printStackTrace();
                }
                break;
            case "delete":
                try {
                    FileWriter.getInstance().removeGame(game);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Invalid input (" + input + "): " + message);
                break;
        }
    }

    protected void setGameId(UUID gameId) {
        this.gameId = gameId;
    }



}
