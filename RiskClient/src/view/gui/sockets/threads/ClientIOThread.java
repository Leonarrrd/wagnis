package view.gui.sockets.threads;


import javafx.application.Platform;
import javafx.concurrent.Task;
import model.Game;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.sockets.GameControllerFacade;

import java.io.EOFException;
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

                String response = null;

                synchronized (reader) {
                    response = reader.readUTF();
                }

                String[] split = response.split(",");
                String event = split[0];
                System.out.println(event);
                switch (event) {
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
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                GUIControl.getInstance().switchToGameScene(UUID.fromString(split[1]));
                            }
                        });


                        break;
                    case CHANGE_UNITS:
                        //split[2] countryName

                        writer.writeUTF(GET_GAME + "," + split[1]);
                        writer.flush();

                        GameControllerFacade.getInstance().game = (Game) reader.readUnshared();
                        String countryName = split[2];

                        GUIControl.getInstance().getComponentMap().get(countryName + "info-hbox").update();

                        break;


                    case GET_GAME:
                        GameControllerFacade.getInstance().game = (Game) reader.readUnshared();
                        break;

                }
            } catch (EOFException e) {
                //This is fine
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        new ErrorAlert(e);
                    }
                });
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}

