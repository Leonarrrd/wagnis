package view.cui;

import java.util.*;

import controller.GameController;

/**
 * A console user interface to guide the user through the game
 * Gives user information about game state and queues him for entries
 * Uses a GameController object to process the game logic
 */
public class ConsoleUserInterface {
    private GameController gc = GameController.getInstance();

    private Scanner reader = new Scanner(System.in);

    /**
     * Method that starts the game and starts the sub cuis.
     */
    public void startGame() {

        AbstractCUI preGameCUI = new PreGameCUI();
        AbstractCUI turnCUI = new TurnCUI();
        preGameCUI.run();
        turnCUI.run();

    }

}