package exceptions;

import model.Card;
import model.Player;

public class CardAlreadyOwnedException extends Exception {
    public CardAlreadyOwnedException(Card card, Player player) {
        super("Card with ID " + card.getId() + " is already owned by " + player.getName() + ".");
    }
}
