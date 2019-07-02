package exceptions;

import model.Card;

import java.io.Serializable;

/**
 * Thrown if there is no such card in the game
 */
public class NoSuchCardException extends Exception implements Serializable{
    private static final long serialVersionUID = 1L;

    public NoSuchCardException(Card card) {
        super("Card with Id " + card.getId() + " should not exist.");
    }
}
