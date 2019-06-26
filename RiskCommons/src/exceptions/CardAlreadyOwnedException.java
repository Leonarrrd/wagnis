package exceptions;

import model.Card;
import model.Player;

import java.io.Serializable;

public class CardAlreadyOwnedException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public CardAlreadyOwnedException(Card card, Player player) {
        super("Card with ID " + card.getId() + " is already owned by " + player.getName() + ".");
    }
}
