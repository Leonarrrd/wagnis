package exceptions;

import model.Card;
import model.Player;

import java.io.Serializable;

public class CardNotOwnedException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;


    public CardNotOwnedException(Card card, Player player) {
        super("Card with ID " + card.getId() + " is not owned by " + player.getName() + ".");
    }
}
