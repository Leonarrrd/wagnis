package persistence;

import exceptions.DuplicateGameIdException;
import exceptions.GameNotFoundException;
import model.Card;
import model.Country;
import model.Game;
import model.Player;
import persistence.helper.GameLoadUtils;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class FileWriter {
    private static FileWriter instance;

    private FileWriter() {}


    public void saveGame(Game game) throws IOException, GameNotFoundException, DuplicateGameIdException {

        boolean newRecord = true;
        for(String s : FileReader.getInstance().loadAvailableGameIds()) {
            if(s.equals(game.getId())) {
                newRecord = false;
            }
        }

        if(newRecord) {
            newGameRecord(game);
        } else {
            updateGameRecord(game);
        }



    }

    private void updateGameRecord(Game game) throws IOException, GameNotFoundException {
        String[] savedGameStrings = FileReader.getInstance().getStringLinesFromData("savedgames.dat");
        List<String> availableGames = FileReader.getInstance().loadAvailableGameIds();
        if(!availableGames.contains(game.getId().toString())) {
            throw new GameNotFoundException("Game with id " + game.getId() + " could not be found." );
        }


        for (int i = 0; i < availableGames.size(); i++) {
            String gameId = savedGameStrings[i].split(",")[0];
            if(savedGameStrings[i].split(",")[0].equals(availableGames.get(i))) {
                savedGameStrings[i] = parseGameToRecord(game);
            }
        }

        StringBuilder sb = new StringBuilder();
        for(String s : savedGameStrings) {
            sb.append(s);
        }

        FileOutputStream out = new FileOutputStream(GameLoadUtils.PROJECT_DATA_DIR + "savedgames.dat");
        byte[] data = sb.toString().getBytes();
        out.write(data);
        out.flush();
        out.close();


    }



    private void newGameRecord(Game game) throws IOException, DuplicateGameIdException {
        boolean duplicateGame = false;
        for(String s : FileReader.getInstance().loadAvailableGameIds()) {
            if(game.getId().toString().equals(s)) {
                duplicateGame = true;
            }
        }
        if(duplicateGame) {
            throw new DuplicateGameIdException("Game with id " + game.getId() + " already exists.");
        }
        outputGameToNewLine(parseGameToRecord(game));
    }

    private String parseGameToRecord(Game game) {
        StringBuilder sb = new StringBuilder();
        //Gameid
        sb.append(game.getId().toString());
        sb.append(",");
        //playerlist
        sb.append("[");
        for(Player p : game.getPlayers()) {
            sb.append(p.getName());
            sb.append(":");
        }
        sb.append("]");
        sb.append(",");
        //Country units relation
        sb.append("{");
        for(Player p : game.getPlayers()) {
            sb.append("[");
            for(Country c: game.getCountries().values()) {
                sb.append(c.getId());
                sb.append("'");
                sb.append(c.getUnits());
                sb.append("-");
            }
            sb.append("]");

        }
        sb.append("}");
        sb.append(",");
        // missions
        sb.append("[");
        for(Player p: game.getPlayers()) {
            sb.append(p.getMission().getId());
            sb.append(":");
        }
        sb.append("]");
        sb.append(",");
        // cards
        sb.append("{");
        for(Player p: game.getPlayers()) {
            sb.append("[");
            for(Card c : p.getCards()) {
                sb.append(c.getId());
                sb.append("-");
            }
            sb.append("]");
            sb.append(":");
        }
        sb.append("}");
        sb.append(",");
        // player that has a turn
        sb.append(game.getTurn().getPlayer().getName());
        sb.append(",");
        // phase
        sb.append(game.getTurn().getPhase().toString());
        sb.append(";\n");

        return sb.toString();
    }

    private void outputGameToNewLine(String data) throws IOException {
        OutputStream outputStream = new FileOutputStream(GameLoadUtils.PROJECT_DATA_DIR + "savedgames.dat", true);
        //TODO: Exception if data is corrupted? might be very complex
        byte[] dataBytes = data.getBytes();
        outputStream.write(dataBytes);
        outputStream.flush();
        outputStream.close();

    }

    public static FileWriter getInstance() {
        if(instance == null) {
            instance = new FileWriter();
        }
        return instance;
    }
}
