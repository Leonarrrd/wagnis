package view.gui.sockets.threads;

import controller.GameController;
import javafx.application.Platform;
import model.Game;
import view.gui.helper.GUIControl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import static helper.Events.PLAYER_JOIN;
import static helper.Events.START_GAME;

public class ClientIOThread extends Thread {

    private ObjectInputStream reader;


    public ClientIOThread(ObjectInputStream reader) {
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

