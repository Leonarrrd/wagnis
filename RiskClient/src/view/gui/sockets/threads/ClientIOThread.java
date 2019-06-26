package view.gui.sockets.threads;


import datastructures.Phase;
import exceptions.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import model.AttackResult;
import model.Game;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.DialogVBox;
import view.gui.boxes.dialogboxes.DefenseVBox;
import view.gui.boxes.dialogboxes.TrailUnitsVBox;
import view.gui.boxes.dialogboxes.UseCardsVBox;
import view.gui.helper.GUIControl;
import view.gui.sockets.GameControllerFacade;

import java.io.*;
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

                String country1Name = null;
                String country2Name = null;
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
                    case SWITCH_TURNS:
                        writer.writeUTF(GET_GAME + "," + split[1]);
                        writer.flush();

                        GameControllerFacade.getInstance().game = (Game) reader.readUnshared();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                GUIControl.getInstance().getComponentMap().get("dialog-vbox").update();
                                GUIControl.getInstance().getComponentMap().get("player-list-vbox").update();
                                GUIControl.getInstance().getComponentMap().get("cards-hbox").update();
                            }
                        });
                        break;
                    case CHANGE_UNITS:
                        //split[2] countryName

                        writer.writeUTF(GET_GAME + "," + split[1]);
                        writer.flush();

                        GameControllerFacade.getInstance().game = (Game) reader.readUnshared();

                        country1Name = split[2];
                        GUIControl.getInstance().getComponentMap().get(country1Name + "info-hbox").update();
                        break;
                    case DEFENSE:
                        String defendingPlayerName = GameControllerFacade.getInstance().game.getCountries().get(split[3]).getOwner().getName();
                        if (GameControllerFacade.getInstance().getPlayerName().equals(defendingPlayerName)) {
                            DialogVBox dialogVBox = (DialogVBox) GUIControl.getInstance().getComponentMap().get("dialog-vbox");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    dialogVBox.getChildren().set(0, new DefenseVBox(split[2], split[3], split[4]));
                                }
                            });
                        }
//                        AttackResult ar = (AttackResult) reader.readUnshared();

                        break;
                    case FIGHT_FINISHED:
                        // split [1} gameId
                        // split [2] attacker
                        // split [3] defender

                        UUID gameId = UUID.fromString(split[1]);
                        Game game = GameControllerFacade.getInstance().getGameById(gameId);
                        String attacker = split[2];
                        String defender = split[3];
                        AttackResult ar = (AttackResult) reader.readUnshared();


                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    GUIControl.getInstance().fight(game.getCountries().get(attacker), game.getCountries().get(defender), ar);
                                } catch (IOException e) {
                                } catch (GameNotFoundException | CountriesNotAdjacentException | NotEnoughUnitsException | NoSuchCountryException | ClassNotFoundException | NoSuchPlayerException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        writer.writeUTF(GET_GAME + "," + split[1]);
                        writer.flush();
                        GameControllerFacade.getInstance().game = (Game) reader.readUnshared();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                GUIControl.getInstance().getComponentMap().get(attacker + "info-hbox").update();
                                GUIControl.getInstance().getComponentMap().get(defender + "info-hbox").update();
                            }
                        });

                        writer.writeUTF(SET_TURN + "," + gameId + "," + Phase.PERFORM_ANOTHER_ATTACK.toString());
                        writer.flush();

                        break;
                    case MOVE:
                        //split[2] country1Name
                        //split[3] country2Name
                        //split[4] trail
                        writer.writeUTF(GET_GAME + "," + split[1]);
                        writer.flush();
                        GameControllerFacade.getInstance().game = (Game) reader.readUnshared();

                        country1Name = split[2];
                        country2Name = split[3];

                        GUIControl.getInstance().getComponentMap().get(country1Name + "info-hbox").update();
                        GUIControl.getInstance().getComponentMap().get(country2Name + "info-hbox").update();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                GUIControl.getInstance().getComponentMap().get("dialog-vbox").update();
                            }
                        });
                        break;
                    case GET_GAME:
                        writer.writeUTF(GET_GAME + "," + split[1]);
                        writer.flush();
                        GameControllerFacade.getInstance().game = (Game) reader.readUnshared();
                        break;
                    case ERROR:
                        Exception e = (Exception) reader.readUnshared();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                new ErrorAlert(e);
                            }
                        });

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
            } catch (ClassNotFoundException | GameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}

