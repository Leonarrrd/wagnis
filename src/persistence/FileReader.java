package persistence;

import datastructures.Phase;
import exceptions.GameNotFoundException;
import exceptions.InvalidFormattedDataException;
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
     * loads countries from countries.csv file and parses them to the needed data structure
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
     * loads continents from continents.csv file and parses them to the needed data structure
     * !!make sure to load continents AFTER loading the countries!!
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

    public List<Mission> loadMissions(List<Continent> continents) throws IOException, InvalidFormattedDataException {
        List<Mission> missions = new ArrayList<>();
        String[] missionData = getStringLinesFromData("missions.dat");

        for (String entry : missionData) {
            String[] fields = entry.split(",");
            int missionId = Integer.parseInt(fields[0]);
            String missionMessage = fields[1];
            switch (fields[2]){
                case "continent":
                    List<Continent> continentsToConquer = new ArrayList<>();
                    int[] continentIndices = getIndicesFromDataSetArray(fields[3]);
                    for (int indice : continentIndices){
                        for (Continent continent : continents){
                            if (indice == continent.getId()){
                                continentsToConquer.add(continent);
                            }
                        }
                    }
                    missions.add(new ContinentMission(missionId, continentsToConquer, missionMessage));
                    break;
                case "country":
                    int countriesToConquer = Integer.parseInt(fields[3]);
                    missions.add(new CountryMission(missionId, countriesToConquer, missionMessage));
                    break;
            }
        }
        Collections.shuffle(missions);
        return missions;
    }

    /*public static void main(String... args) throws IOException, GameNotFoundException, InvalidFormattedDataException {
        List<Country> countries = new ArrayList(getInstance().loadCountries().values());
        List<Continent> continents = getInstance().loadContinents(countries);
        getInstance().loadGame(UUID.fromString("c16f70fc-1fa2-41a5-919c-2332e97debf2"), countries, continents);
    }*/

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

    public Game loadGame(UUID gameId, List<Country> loadedCountries, List<Continent> loadedContinents, List<Mission> loadedMissions) throws IOException, GameNotFoundException, InvalidFormattedDataException {
        String[] gameData = getStringLinesFromData("savedgames.dat");
        String dataset = null;

        boolean validGameId = false;
        for(String s : loadAvailableGameIds()) {
            if(s.equals(gameId.toString())) {
                validGameId = true;
            }
        }

        if(!validGameId) {
            throw new GameNotFoundException("There is no game id with " + gameId.toString() + "saved.");
        }

        //evaluating the right dataset for the gameId
        for (String entry : gameData) {
            String[] split = entry.split(",");
            if (split[0].equals((gameId.toString()))) {
                dataset = entry;
            }
        }

        if (dataset == null || dataset.isEmpty()) {
            throw new GameNotFoundException("Game with id " + gameId.toString() + "could not be found.");
        }

        String[] commaSplit = dataset.split(",");

        //Determine gameId
        UUID nGameId = UUID.fromString(commaSplit[0]);

        //evaluate Player array
        String[] playerNames = GameLoadUtils.evaluatePlayerArrayFromDatString(commaSplit[1]);
        List<Player> players = new ArrayList();
        for (String s : playerNames) {
            players.add(new Player(s));
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

        //evaluate missions
        //TODO: Evaluating missions

        //evaluate cards
        //TODO: Evaluating cards

        //evaluate player turn
        Player activePlayer = null;
        for (Player p : players) {
            if (p.getName().equals(commaSplit[5])) {
                activePlayer = p;
            }
        }
        if (activePlayer == null) {
            throw new InvalidFormattedDataException("The active player could not be restored from Save file.");
        }
        //evaluate turn phase
        Phase phase = null;
        switch (commaSplit[6]) {
            case "ATTACK":
                phase = Phase.ATTACK;
                break;
            case "PLACE_UNITS":
                phase = Phase.PLACE_UNITS;
                break;
            case "MOVE":
                phase = Phase.MOVE;
                break;
            default:
                throw new InvalidFormattedDataException("The turn phase could not be restored from save file.");
        }

        Turn turn = new Turn(activePlayer, phase);

        Map<String, Country> loadedCountriesMap = new HashMap<>();
        for (Country c : loadedCountries) {
            loadedCountriesMap.put(c.getName(), c);
        }

        //FIXME: total bogus third parameter
        Game game = new Game(gameId, loadedCountriesMap, loadedContinents, loadedMissions);
        game.setPlayers(players);
        game.setTurn(turn);

        return game;
    }


    private int[] getIndicesFromDataSetArray(String arrayString) throws InvalidFormattedDataException {
        if (!arrayString.contains("[") && !arrayString.contains("]")) {
            throw new InvalidFormattedDataException("Array does not contain [ and ]. Use following format: [0:1:...:n]");
        }

        arrayString = arrayString.replace("[", "").replace("]", "");
        String[] split = arrayString.split(":");
        int[] indices = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            indices[i] = Integer.parseInt(split[i]);
        }

        return indices;
    }

    String[] getStringLinesFromData(String dataFile)  throws IOException {

        String toSplit = loadFileContent(dataFile);
        String[] split = toSplit.split(";");


        return split;
    }

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
