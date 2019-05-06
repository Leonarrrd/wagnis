package view.cui;

import controller.GameController;

import java.util.Scanner;
import java.util.UUID;

public abstract class AbstractCUI {

    protected GameController gc = GameController.getInstance();
    protected Scanner reader = new Scanner(System.in);
    protected static UUID gameId;


    public abstract void run();

    /**
     * if the input from the call is not a valid input for a player action
     * this method checks if it's a special input (like printing the game state with "state"
     * else the input is invalid and an error message is printed.
     *
     * @param input
     * @param gameState
     */
    protected void checkForSpecialInput(String input, String message, String gameState) {
        switch (input) {
            case "state":
                System.out.println(gameState + "\n");
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input (" + input + "): " + message);
        }
    }

    protected void setGameId(UUID gameId) {
        this.gameId = gameId;
    }



}
