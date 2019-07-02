package exceptions;

import model.Card;
import model.Player;

import java.io.Serializable;

/**
 * MARK: Not used at the moment, but might be helpful later
 * If a player uses a card that he does not own.
 */
public class CardNotOwnedException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;


    public CardNotOwnedException(Card card, Player player) {
        super("Card with ID " + card.getId() + " is not owned by " + player.getName() + ".");
    }
}
