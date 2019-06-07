package exceptions;

import model.Player;

public class NoSuchPlayerException extends Exception {
    public NoSuchPlayerException(Player player) {
        super("Player " + player.getName() + " should not exist.");
    }
}
