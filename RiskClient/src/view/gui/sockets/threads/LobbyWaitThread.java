package view.gui.sockets.threads;

import javafx.application.Platform;
import view.gui.helper.GUIControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;

public class LobbyWaitThread extends Thread {

    private BufferedReader reader;

    public LobbyWaitThread(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        System.out.println("waiting for response...");

        try {
            String response = reader.readLine();
            if (response != null) {
                String[] split = response.split(",");
                if (split[0] != null) {
                    if (split[0].equals("playerjoin")) {
                        //TODO: GUI update lobby
                        System.out.println("Player " + split[1] + " joined lobby");
                        GUIControl.getInstance().getPlayersInLobby().add(split[1]);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                GUIControl.getInstance().playerJoined();
                            }
                        });

                    }
                    if (split[0].equals("gamestart")) {
                        //TODO: GUI update game
                        System.out.println("Game started");

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
};

