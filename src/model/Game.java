package model;

import java.util.*;

/*
 * Class for Game Objects
 * Holds (all?) information about the current game state
 * Could potentially be amplified to hold all game information so that we could save and load games
 */
public class Game {

    private UUID id;

    private List<Player> players = new ArrayList<Player>();

    private Map<String, Country> countries;

    private List<Continent> continents = new ArrayList<>();

    private List<Mission> missions = new ArrayList<>();

    private List<Card> cardDeck = new ArrayList<>();

    private Turn turn;

    /*
     * Just filling the map with generic countries
     */
    public Game(UUID id, Map<String, Country> countries, List<Continent> continents, List<Mission> missions) {

        this.id = id;
        this.countries = countries;
        this.continents = continents;
        this.missions = missions;

        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 2; i++) {
                cardDeck.add(new Card(j+i, i + 1));
            }
        }
        Collections.shuffle(cardDeck);
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
            sb.append(p.toString() + "\n");
        }
        sb.append("\nCountries: \n");
        for (Country c : this.countries.values()) {
            sb.append(c.printRepresantation() + "\n");
        }
        sb.append("\nContinents: \n");

        for (Continent c : this.continents) {
            sb.append(c.toString() + "\n");
        }

        return sb.toString();

    }


}
