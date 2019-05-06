package persistence;

import java.io.File;
import java.io.IOException;
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

    public void saveNewGameRecord() {

    }

    public static FileWriter getInstance() {
        if(instance == null) {
            instance = new FileWriter();
        }
        return instance;
    }
}
