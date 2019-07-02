package exceptions;

import model.Player;

import java.io.Serializable;

/**
 * Thrown if no such player is in the game
 */
public class NoSuchPlayerException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NoSuchPlayerException(Player player) {
        super("Player " + player.getName() + " should not exist.");
    }
}
