package view.gui.sockets.threads;

import controller.GameController;
import exceptions.*;
import javafx.application.Platform;
import model.Game;
import view.gui.helper.GUIControl;
import view.gui.sockets.GameControllerFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static helper.Events.PLAYER_JOIN;
import static helper.Events.START_GAME;

public class LobbyWaitThread extends Thread {

    private BufferedReader reader;


    public LobbyWaitThread(BufferedReader reader) {
        this.reader = reader;

    }

    @Override
    public void run() {

        System.out.println("waiting for response...");
        while (true) {
            try {
                String response = reader.readLine();
                if (response != null) {
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

                        }
                        if (split[0].equals(START_GAME)) {
                            //TODO: GUI update game
                            System.out.println("Game started");
                            UUID gameId = UUID.fromString(split[1]);


                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        GameControllerFacade.getInstance().initNewGame(gameId);
                                    } catch (IOException | InvalidFormattedDataException | InvalidPlayerNameException | MaximumNumberOfPlayersReachedException | CountriesAlreadyAssignedException | GameNotFoundException | NoSuchPlayerException e) {
                                        e.printStackTrace();
                                    }
                                    GUIControl.getInstance().switchToGameScene(gameId);
                                }
                            });

                            this.interrupt();

                        }
                    }
                }

            } catch (IOException  e) {
                e.printStackTrace();
            }


        }
    }
};

