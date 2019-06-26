package exceptions;

import model.Player;

import java.io.Serializable;

public class NoSuchPlayerException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NoSuchPlayerException(Player player) {
        super("Player " + player.getName() + " should not exist.");
    }
}
