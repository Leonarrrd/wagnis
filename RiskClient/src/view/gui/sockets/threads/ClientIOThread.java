package view.gui.sockets.threads;

import controller.GameController;
import javafx.application.Platform;
import model.Game;
import view.gui.helper.GUIControl;
import view.gui.sockets.GameControllerFacade;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static helper.Events.*;

public class ClientIOThread extends Thread {

    private ObjectInputStream reader;
    private ObjectOutputStream writer;


    public ClientIOThread(ObjectInputStream reader, ObjectOutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run() {

        System.out.println("waiting for response...");
        while (true) {
            try {
                String response = reader.readUTF();
                //TODO: Might add create game to make a list of open games?
                // No need to check for response != null
                String[] split = response.split(",");
                String event = split[0];
                System.out.println(event);
                switch(event) {
                    case PLAYER_JOIN:
                        List<String> playerList = new ArrayList<>();
                        for (int i = 1; i <= split.length - 1; i++) {
                            playerList.add(split[i]);
                        }
                        GUIControl.getInstance().setPlayersInLobby(playerList);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                GUIControl.getInstance().playerJoined();

                            }
                        });
                        break;
                    case START_GAME:
                        GUIControl.getInstance().switchToGameScene(UUID.fromString(split[1]));
                        break;
                    case PLACE_UNITS:
                        //split[1] countryName
                        String countryName = split[1];

                        GUIControl.getInstance().getComponentMap().get(countryName + "info-hbox").update();
                        break;
                }

            } catch (IOException  e) {
                //e.printStackTrace();
            }


        }
    }

}

