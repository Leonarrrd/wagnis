package exceptions;

import model.Card;

public class NoSuchCardException extends Exception {
    public NoSuchCardException(Card card) {
        super("Card with Id " + card.getId() + " should not exist.");
    }
}
