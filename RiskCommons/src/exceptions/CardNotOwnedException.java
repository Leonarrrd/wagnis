package exceptions;

import model.Card;
import model.Player;

public class CardNotOwnedException extends Exception {

    public CardNotOwnedException(Card card, Player player) {
        super("Card with ID " + card.getId() + " is not owned by " + player.getName() + ".");
    }
}
