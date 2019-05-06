package controller;

import exceptions.CardAlreadyOwnedException;
import exceptions.GameNotFoundException;
import exceptions.NoSuchCardException;
import exceptions.NoSuchPlayerException;
import model.Card;
import model.Game;
import model.Player;

import java.util.UUID;

public class CardDeckController {
    private static CardDeckController instance;

    private CardDeckController() {
    }

    public static CardDeckController getInstance() {
        if (instance == null) {
            instance = new CardDeckController();
        }
        return instance;
    }

    void addCard(Game game, Player player) throws NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }
        //!!FIXME:absolute bullshit, but wanted to get the exception in fix: pass right card
        Card card = game.getCardDeck().get(0);
        if(player.getCards().contains(card)) {
            throw new CardAlreadyOwnedException(player + " already owns the card.");
        }
        if (!game.getCardDeck().contains(card)) {
            throw new NoSuchCardException("Card does not exist");
        }

        player.getCards().add(game.getCardDeck().get(0));
        game.getCardDeck().remove(0);
    }
}
