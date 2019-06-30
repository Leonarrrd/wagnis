package persistence;

import controller.GameController;
import controller.GraphController;
import datastructures.Color;
import datastructures.Phase;
import exceptions.GameNotFoundException;
import exceptions.InvalidFormattedDataException;
import exceptions.NoSuchPlayerException;
import model.*;
import persistence.helper.GameLoadUtils;
import persistence.helpermodels.RawCountryData;

import java.io.*;
import java.util.*;

public class FileReader {

    private static FileReader instance;

    private FileReader() {
    }


    /**
     * Loads countries from countries.dat file and parses them to the needed data structure
     *
     * @return
     * @throws IOException
     */
    public Map<String, Country> loadCountries() throws IOException, NumberFormatException, ArrayIndexOutOfBoundsException, InvalidFormattedDataException {
        List<Country> loadedCountries = new ArrayList();

        String[] countryData = new FileReader().getStringLinesFromData("countries.dat");


        //saving the indices to the hashmap, because the countries need to be initialized before we're able to assign
        //neighbour countries to the specific country because the country to be added might not have been loaded yet.
        Map<Country, int[]> countryNeighbourIndecesMap = new HashMap<>();
        for (String entry : countryData) {
            String[] fields = entry.split(",");
            int countryId = Integer.parseInt(fields[0]);
            String countryName = fields[1];
            int[] countryIndeces = getIndicesFromDataSetArray(fields[2]);
            Country c = new Country(countryId, countryName);
            countryNeighbourIndecesMap.put(c, countryIndeces);
        }

        for (Map.Entry<Country, int[]> entry : countryNeighbourIndecesMap.entrySet()) {
            Country c = entry.getKey();
            List<Country> neighbourCountries = new ArrayList();
            for (Country key : countryNeighbourIndecesMap.keySet()) {
                for (int i : entry.getValue()) {
                    if (i == key.getId()) {
                        neighbourCountries.add(key);
                    }
                }
            }
            c.setNeighbors(neighbourCountries);
            loadedCountries.add(c);
        }

        Map<String, Country> mappedCountries = new HashMap();

        for (Country c : loadedCountries) {
            mappedCountries.put(c.getName(), c);
        }

        return mappedCountries;
    }

    /**
     * Loads continents from continents.dat file and parses them to the needed data structure
     * !!Make sure to load continents AFTER loading the countries!!
     *
     * @return
     * @throws IOException
     */
    public List<Continent> loadContinents(List<Country> countries) throws IOException, InvalidFormattedDataException {
        List<Continent> continents = new ArrayList();
        String[] continentData = getStringLinesFromData("continents.dat");

        for (String entry : continentData) {
            String[] fields = entry.split(",");
            int continentId = Integer.parseInt(fields[0]);
            String continentName = fields[1];
            int bonusUnits = Integer.parseInt(fields[2]);
            int[] countryIndeces = getIndicesFromDataSetArray(fields[3]);
            List<Country> includingCountries = new ArrayList();
            for (Country c : countries) {
                for (int i : countryIndeces) {
                    if (i == c.getId()) {
                        includingCountries.add(c);
                    }
                }
            }

            Continent c = new Continent(continentId, continentName, bonusUnits, includingCountries);
            continents.add(c);

        }

        return continents;
    }

    /**
     * Loads missions from the persistence (missions.dat).
     * @param continents
     * @return
     * @throws IOException
     * @throws InvalidFormattedDataException
     */
    public List<Mission> loadMissions(List<Continent> continents) throws IOException, InvalidFormattedDataException {
        List<Mission> missions = new ArrayList<>();
        String[] missionData = getStringLinesFromData("missions.dat");

        for (String entry : missionData) {
            String[] fields = entry.split(",");
            int missionId = Integer.parseInt(fields[0]);
            switch (fields[1]){
                case "continent":
                    List<Continent> continentsToConquer = new ArrayList<>();
                    int[] continentIndices = getIndicesFromDataSetArray(fields[2]);
                    for (int indice : continentIndices){
                        for (Continent continent : continents){
                            if (indice == continent.getId()){
                                continentsToConquer.add(continent);
                            }
                        }
                    }
                    missions.add(new ContinentMission(missionId, continentsToConquer));
                    break;
                case "country":
                    int countriesToConquer = Integer.parseInt(fields[2]);
                    missions.add(new CountryMission(missionId, countriesToConquer));
                    break;
                case "elimination":
                    Color targetColor = null;
                    for (Color color : Color.values()){
                        if (color.toString().equals(fields[2]))
                            targetColor = color;
                    }
                    List<Continent> continentsToConquerE = new ArrayList<>();
                    int[] continentIndicesE = getIndicesFromDataSetArray(fields[3]);
                    for (int indice : continentIndicesE){
                        for (Continent continent : continents){
                            if (indice == continent.getId()){
                                continentsToConquerE.add(continent);
                            }
                        }
                    }
                    missions.add(new EliminationMission(missionId, targetColor, continentsToConquerE));
                    break;
            }
        }
        Collections.shuffle(missions);
        return missions;
    }

    /**
     * Loads available saved games
     * @return
     * @throws IOException
     */
    public List<String> loadAvailableGameIds() throws IOException {
        String[] gameData = getStringLinesFromData("savedgames.dat");
        List<String> availableGameIds = new ArrayList<>();
        for(String entry: gameData) {
            String[] split = entry.split(",");
            //TODO: Map game Ids to something more typeable
            availableGameIds.add(split[0]);
        }

        return availableGameIds;
    }

    /**
     * loads a game from the persistence and parses it into a valid Game-Object.
     * The assets (country, continents, missions, cards) need to be loaded first and passed as parameters.
     * @param gameId
     * @param loadedCountries
     * @param loadedContinents
     * @param loadedMissions
     * @param loadedCards
     * @return
     * @throws IOException
     * @throws GameNotFoundException
     * @throws InvalidFormattedDataException
     */
    public Game loadGame(UUID gameId, List<Country> loadedCountries, List<Continent> loadedContinents, List<Mission> loadedMissions, List<Card> loadedCards) throws IOException, GameNotFoundException, InvalidFormattedDataException, NoSuchPlayerException {
        String[] gameData = getStringLinesFromData("savedgames.dat");
        String dataset = null;

        boolean validGameId = false;
        for(String s : loadAvailableGameIds()) {
            if(s.equals(gameId.toString())) {
                validGameId = true;
            }
        }

        if(!validGameId) {
            throw new GameNotFoundException(gameId);
        }

        //evaluating the right dataset for the gameId
        for (String entry : gameData) {
            String[] split = entry.split(",");
            if (split[0].equals((gameId.toString()))) {
                dataset = entry;
            }
        }

        if (dataset == null || dataset.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }

        String[] commaSplit = dataset.split(",");

        //evaluate Player array
        String[] playerNames = GameLoadUtils.evaluatePlayerArrayFromDataString(commaSplit[1]);
        List<Player> players = new ArrayList();
        for (String s : playerNames) {
            players.add(new Player(s));
        }


        //evaluate owned countries by players + units
        //Integer = player index, RawCountryData countryId + units
        Map<Integer, RawCountryData[]> countryDataMap = GameLoadUtils.evaluateCountryData(commaSplit[2]);
        for (Map.Entry<Integer, RawCountryData[]> entry : countryDataMap.entrySet()) {
            Player player = players.get(entry.getKey());
            List<Country> assignableCountries = new ArrayList();
            for (RawCountryData rcd : entry.getValue()) {
                for (Country c : loadedCountries) {
                    if (rcd.getId() == c.getId()) {
                        c.setUnits(rcd.getUnits());
                        assignableCountries.add(c);
                        c.setOwner(player);
                    }
                }
            }
            Map<String, Country> playerCountryMap = new HashMap<>();
            for (Country c : assignableCountries) {
                playerCountryMap.put(c.getName(), c);

            }

            player.setCountries(playerCountryMap);

        }

        //evaluate player missions
        List<Integer> missionIds = GameLoadUtils.evaluateMissionData(commaSplit[3]);
        for (int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            for (Mission mission : loadedMissions){
                if (missionIds.get(i) == mission.getId()){
                    player.setMission(mission);
                }
            }
        }
        //evaluate cards
        //TODO: Evaluating cards
        //TODO: Create cardDeck
        // key is the player index, values or List of cardIds
        List<Card> cardDeck = (ArrayList)((ArrayList)loadedCards).clone();

        Map<Integer, List<Integer>> cardData = GameLoadUtils.evaluateCardData(commaSplit[4]);
        for (int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            List<Card> playersCards = new ArrayList<>();
            if (cardData.get(i) != null) {
                for (int cardId : cardData.get(i)) {
                    for (Card card : loadedCards) {
                        if (cardId == card.getId()) {
                            playersCards.add(card);
                            cardDeck.remove(card);
                        }
                    }
                }
            }
            player.setCards(playersCards);
        }


        //evaluate player turn
        Player activePlayer = null;
        for (Player p : players) {
            if (p.getName().equals(commaSplit[5])) {
                activePlayer = p;
            }
        }
        if (activePlayer == null) {
            throw new InvalidFormattedDataException();
        }
        //evaluate turn phase
        Phase phase = null;
        switch (commaSplit[6]) {
            case "USE_CARDS":
                phase = Phase.USE_CARDS;
                break;
            case "PLACE_UNITS":
                phase = Phase.PLACE_UNITS;
                break;
            case "ATTACK":
                phase = Phase.ATTACK;
                break;
            case "TRAIL_UNITS":
                phase = Phase.TRAIL_UNITS;
                break;
            case "PERFORM_ANOTHER_ATTACK":
                phase = Phase.PERFORM_ANOTHER_ATTACK;
                break;
            case "PERFORM_ANOTHER_MOVE":
                phase = Phase.PERFORM_ANOTHER_MOVE;
                break;
            case "MOVE":
                phase = Phase.MOVE;
                break;

            default:
                throw new InvalidFormattedDataException();
        }

        Turn turn = new Turn(activePlayer, phase);

        Map<String, Country> loadedCountriesMap = new HashMap<>();
        for (Country c : loadedCountries) {
            loadedCountriesMap.put(c.getName(), c);
        }

        Game game = new Game(gameId, loadedCountriesMap, loadedContinents, loadedMissions, loadedCards, cardDeck);
        game.setPlayers(players);
        game.setTurn(turn);

        for (int i = 0; i < game.getPlayers().size(); i++){
            Player player = game.getPlayers().get(i);
            GraphController.getInstance().updateGraph(game, player);
            player.setColor(Color.values()[i]);
        }

        return game;
    }


    /**
     * helper method to determine the indices from a String in the persistence representation.
     * @param arrayString
     * @return
     * @throws InvalidFormattedDataException
     */
    private int[] getIndicesFromDataSetArray(String arrayString) throws InvalidFormattedDataException {
        if (!arrayString.contains("[") && !arrayString.contains("]")) {
            throw new InvalidFormattedDataException();
        }

        arrayString = arrayString.replace("[", "").replace("]", "");
        String[] split = arrayString.split(":");
        int[] indices = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            indices[i] = Integer.parseInt(split[i]);
        }

        return indices;
    }

    /**
     * Loads each line (delmitted by ';') from a file and puts it into a String array.
     * @param dataFile
     * @return
     * @throws IOException
     */
    String[] getStringLinesFromData(String dataFile)  throws IOException {

        String toSplit = loadFileContent(dataFile);
        String[] split = toSplit.split(";");


        return split;
    }

    /**
     * Loads the whole content of a file
     * @param dataFile
     * @return
     * @throws IOException
     */
    private String loadFileContent(String dataFile)  throws IOException  {
        InputStream inputStream = new FileInputStream(GameLoadUtils.PROJECT_DATA_DIR + dataFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }


    public static FileReader getInstance() {
        if (instance == null) {
            instance = new FileReader();
        }
        return instance;
    }
}
