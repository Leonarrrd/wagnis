package persistence;

import model.Card;
import model.Country;
import model.Game;
import model.Player;
import persistence.helper.GameLoadUtils;

import java.io.*;
import java.util.UUID;

public class FileWriter {
    private static FileWriter instance;

    private FileWriter() {}


    public void saveGame(UUID gameId) throws IOException {

        boolean newRecord = true;
        for(String s : FileReader.getInstance().loadAvailableGameIds()) {
            if(s.equals(gameId.toString())) {
                newRecord = false;
            }
        }



    }

    public void updateGameRecord() {

    }

    public void saveGame(Game game) throws IOException {
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
        sb.append(";");

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
