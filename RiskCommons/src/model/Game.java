package model;

import java.io.Serializable;
import java.util.*;

/*
 * Class for Game Objects
 * Holds (all?) information about the current game state
 * Could potentially be amplified to hold all game information so that we could save and load games
 */
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID id;
    private List<Player> players = new ArrayList<Player>();
    private Map<String, Country> countries;
    private List<Continent> continents;
    private List<Mission> missions;
    private List<Card> cards;
    private List<Card> cardDeck;
    private Turn turn;

    public Game(UUID id, Map<String, Country> countries, List<Continent> continents, List<Mission> missions, List<Card> cards, List<Card> cardDeck) {
        this.id = id;
        this.countries = countries;
        this.continents = continents;
        this.missions = missions;
        this.cards = cards;
        this.cardDeck = cardDeck;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Map<String, Country> getCountries() {
        return countries;
    }

    public List<Continent> getContinents() {
        return continents;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public List<Card> getCardDeck() {
        return cardDeck;
    }

    public List<Card> getCards(){
        return cards;
    }

    public UUID getId() {
        return id;
    }

    public Turn getTurn() {
        return turn;
    }
    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game " + this.id.toString());
        sb.append("\n");
        sb.append("\nPlayers: " + "\n");
        for (Player p : this.players) {
            sb.append(p.toString() + "| ");
            sb.append("Mission: " + p.getMission().getMessage() + "| ");
            sb.append("Cards: " + p.getCards());
            sb.append("\n");
        }
        sb.append("\nCountries: \n");
        for (Country c : this.countries.values()) {
            sb.append(c.printRepresentation() + "\n");
        }
        sb.append("\nContinents: \n");

        for (Continent c : this.continents) {
            sb.append(c.toString() + "\n");
        }

        sb.append("\n Turn: Player " + this.turn.getPlayer() + "'s turn. Phase: " + this.turn.getPhase().toString() );

        return sb.toString();

    }


}
