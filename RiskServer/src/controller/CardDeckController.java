package controller;

import datastructures.CardSymbol;
import exceptions.CardAlreadyOwnedException;
import exceptions.NoSuchCardException;
import exceptions.NoSuchPlayerException;
import model.Card;
import model.Game;
import model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller that needs to control the card mechanism
 */
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

    /**
     * Adds a card to a player
     *
     * @param game needs to be provided to identify the correct game
     * @param player
     * @throws NoSuchPlayerException
     * @throws NoSuchCardException
     * @throws CardAlreadyOwnedException
     */
    void addCardToPlayer(Game game, Player player) throws NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }
        Card card = game.getCardDeck().get(0);
        if(player.getCards().contains(card)) {
            throw new CardAlreadyOwnedException(card, player);
        }
        if (!game.getCardDeck().contains(card)) {
            throw new NoSuchCardException(card);
        }

        player.getCards().add(game.getCardDeck().remove(0));
    }


    /**
     * Creates the card deck
     * @return List<Card> Shuffled Cards
     */
    List<Card> createCardDeck(){
        List<Card> cardDeck = new ArrayList<>();
        int id = 1;
        for (CardSymbol symbol : CardSymbol.values()){
            for (int i = 0; i < 10; i++){
                cardDeck.add(new Card(id, symbol));
                id++;
            }
        }
        Collections.shuffle(cardDeck);
        return cardDeck;
    }
}
