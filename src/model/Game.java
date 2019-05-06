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
    public Game(UUID id, Map<String, Country> countries, List<Continent> continents) {

        this.id = id;
        this.countries = countries;
        this.continents = continents;

        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 2; i++) {
                cardDeck.add(new Card(i + 1));
            }
        }
        Collections.shuffle(cardDeck);

//        Country germany = new Country("Germany");
//        Country denmark = new Country("Denmark");
//        Country sweden = new Country("Sweden");
//        Country finland = new Country("Finland");
//        Country norway = new Country("Norway");
//        Country poland = new Country("Poland");
//
//        germany.setNeighbors(new ArrayList<Country>(
//                Arrays.asList(denmark, poland)
//        ));
//        denmark.setNeighbors(new ArrayList<Country>(
//                Arrays.asList(sweden, germany)
//        ));
//        sweden.setNeighbors(new ArrayList<Country>(
//                Arrays.asList(norway, denmark, finland)
//        ));
//        norway.setNeighbors(new ArrayList<Country>(
//                Arrays.asList(sweden, finland)
//        ));
//        finland.setNeighbors(new ArrayList<Country>(
//                Arrays.asList(norway, sweden, poland)
//        ));
//        poland.setNeighbors(new ArrayList<Country>(
//                Arrays.asList(germany, finland)
//        ));
//
//
//        countries = new HashMap<String, Country>() {{
//            put("Germany", germany);
//            put("Denmark", denmark);
//            put("Sweden", sweden);
//            put("Finland", finland);
//            put("Norway", norway);
//            put("Poland", poland);
//        }};
//
//        continents.add(new Continent(1,"GermanyAndPoland", 5, Arrays.asList(germany, poland)));

//        missions.add(new Mission(players.get(1))); // kill player 2
//        missions.add(new Mission(players.get(0))); // kill player 1
//        missions.add(new Mission(Arrays.asList(continents.get(0)))); // conquer Germany and Poland
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
