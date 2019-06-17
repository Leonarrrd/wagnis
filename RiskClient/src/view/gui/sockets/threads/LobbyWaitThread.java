package view.gui.sockets.threads;

import controller.GameController;
import exceptions.*;
import javafx.application.Platform;
import model.Game;
import view.gui.helper.GUIControl;
import view.gui.sockets.GameControllerFacade;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static helper.Events.PLAYER_JOIN;
import static helper.Events.START_GAME;

public class LobbyWaitThread extends Thread {

    private ObjectInputStream reader;


    public LobbyWaitThread(ObjectInputStream reader) {
        this.reader = reader;

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
                if (split[0] != null) {
                    if (split[0].equals(PLAYER_JOIN)) {
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

                    } else if (split[0].equals(START_GAME)) {
                        Game game = (Game) reader.readObject();

                        //FIXME: NOOOOOW!!!
                        GameController.getInstance().activeGames.put(game.getId(), game);
                        GUIControl.getInstance().switchToGameScene(game.getId());

                    }


                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

}

