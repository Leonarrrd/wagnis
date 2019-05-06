package controller;

import exceptions.GameNotFoundException;
import exceptions.NoSuchPlayerException;
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

    void addCard(Game game, Player player) throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }

        player.getCards().add(game.getCardDeck().get(0));
        game.getCardDeck().remove(0);
    }
}
