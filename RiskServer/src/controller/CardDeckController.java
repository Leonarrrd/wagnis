package controller;

import exceptions.CardAlreadyOwnedException;
import exceptions.NoSuchCardException;
import exceptions.NoSuchPlayerException;
import model.Card;
import model.Game;
import model.Player;

import java.util.ArrayList;
import java.util.List;

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
        Card card = game.getCardDeck().get(0);
        if(player.getCards().contains(card)) {
            throw new CardAlreadyOwnedException(player + " already owns the card.");
        }
        if (!game.getCardDeck().contains(card)) {
            throw new NoSuchCardException("Card does not exist");
        }

        player.getCards().add(game.getCardDeck().remove(0));
    }

    List<Card> createCardDeck(){
        List<Card> cardDeck = new ArrayList<>();
        int id = 1;
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 2; i++) {
                cardDeck.add(new Card(id,i + 1));
                id++;
            }
        }
        return cardDeck;
    }
}
