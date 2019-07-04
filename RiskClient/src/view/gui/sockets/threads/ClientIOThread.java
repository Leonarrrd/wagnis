package view.gui.sockets.threads;


import datastructures.Phase;
import exceptions.GameNotFoundException;
import javafx.application.Platform;
import model.AttackResult;
import model.Game;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.DialogVBox;
import view.gui.boxes.JoinGameVBox;
import view.gui.boxes.dialogboxes.DefenseVBox;
import view.gui.helper.GUIControl;
import view.gui.lists.SavedGamesListView;
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

                String country1Name = null;
                String country2Name = null;
                switch (event) {
                    case LOAD_AVAILABLE_GAME_IDS:
                        //split[1-x] gameIds
                        List<String> gameIds = new ArrayList<>();
                        for (String s : split) {
                            gameIds.add(s);
                        }
                        // remove the event from the list
                        gameIds.remove(0);
                        loadAvailableGameIds(gameIds);

                        break;
                    case CHECK_GAME_TYPE:
                        // split[1] gameId
                        // split[2] gameType
                        // split[3-x] playerNames
                        List<String> playerNames = new ArrayList<>();
                        for (int i = 3; i < split.length; i++) {
                            playerNames.add(split[i]);
                        }
                        checkGameType(split[1], split[2], playerNames);
                        break;
                    case PLAYER_JOIN:
                        // split[1-x] playerNames
                        List<String> playerList = new ArrayList<>();
                        for (int i = 1; i <= split.length - 1; i++) {
                            playerList.add(split[i]);
                        }
                        playerJoin(playerList);
                        break;
                    case START_GAME:
                        startGame(split[1]);
                        break;
                    case SWITCH_TURNS:
                        // split[1] gameId
                        switchTurns(split[1]);
                        break;
                    case CHANGE_UNITS:
                        // split[1] gameId
                        // split[2] country
                        changeUnits(split[1], split[2]);
                        break;
                    case DEFENSE:
                        // split[1] attackingCountry
                        // split[2] defendingCountry
                        // split[3] units
                        defense(split[1], split[2], split[3]);
                        break;
                    case FIGHT_FINISHED:
                        // split [1] gameId
                        // split [2] attacker
                        // split [3] defender
                        fightFinished(split[1], split[2], split[3]);

                        break;
                    case MOVE:
                        //split[1] gameId
                        //split[2] srcCountry
                        //split[3] destCountry
                        //split[4] trail
                        move(split[1], split[2], split[3], split[4]);
                        break;
                    case END_GAME:
                        System.out.println("end game...");
                        endGame(split[1]);
                        break;
                    case REMOVE_PLAYER:
                        removePlayer();
                        break;
                    case GET_GAME:
                        // split[1] gameId
                        getGame(split[1]);
                        break;
                    case ERROR:
                        error();
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

    private void loadAvailableGameIds(List<String> gameIds) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SavedGamesListView lv = (SavedGamesListView) GUIControl.getInstance().getComponentMap().get("saved-games-list-view");
                lv.createList(gameIds);
            }
        });
    }

    private void checkGameType(String gameId, String gameType, List<String> playerNames) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JoinGameVBox jgvb = (JoinGameVBox) GUIControl.getInstance().getComponentMap().get("join-game-vbox");
                jgvb.handleGameType(gameId, gameType, playerNames);
            }
        });
    }

    private void playerJoin(List<String> playerNames) {

        GUIControl.getInstance().setPlayersInLobby(playerNames);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIControl.getInstance().playerJoined();
            }
        });
    }

    private void startGame(String gameId) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIControl.getInstance().switchToGameScene(UUID.fromString(gameId));
            }
        });
    }

    private void removePlayer() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIControl.getInstance().returnToLobby(true);
            }
        });
    }

    private void endGame(String winner) {
        boolean isWinner = winner.equals(GameControllerFacade.getInstance().getPlayerName());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIControl.getInstance().returnToLobby(!isWinner);
            }
        });
    }

    private void getGame(String gameId) throws IOException, ClassNotFoundException {
        writer.writeUTF(GET_GAME + "," + gameId);
        writer.flush();
        Game getGame = (Game) reader.readUnshared();
        GameControllerFacade.getInstance().setGame(getGame);
    }

    private void switchTurns(String gameId) throws IOException, ClassNotFoundException {
        writer.writeUTF(GET_GAME + "," + gameId);
        writer.flush();

        Game switchTurnsGame = (Game) reader.readUnshared();
        GameControllerFacade.getInstance().setGame(switchTurnsGame);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIControl.getInstance().getComponentMap().get("dialog-vbox").update();
                GUIControl.getInstance().getComponentMap().get("player-list-vbox").update();
                GUIControl.getInstance().getComponentMap().get("cards-hbox").update();
            }
        });
    }

    private void changeUnits(String gameId, String countryString) throws IOException, ClassNotFoundException {

        writer.writeUTF(GET_GAME + "," + gameId);
        writer.flush();

        Game changeUnitsGame = (Game) reader.readUnshared();
        GameControllerFacade.getInstance().setGame(changeUnitsGame);

        GUIControl.getInstance().getComponentMap().get(countryString + "info-hbox").update();
    }

    private void defense(String attackingCountry, String defendingCountry, String units) {
        String defendingPlayerName = GameControllerFacade.getInstance().getGame().getCountries().get(defendingCountry).getOwner().getName();
        if (GameControllerFacade.getInstance().getPlayerName().equals(defendingPlayerName)) {
            DialogVBox dialogVBox = (DialogVBox) GUIControl.getInstance().getComponentMap().get("dialog-vbox");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    dialogVBox.getChildren().set(0, new DefenseVBox(attackingCountry, defendingCountry, units));
                }
            });
        }
    }

    private void fightFinished(String gameIdString, String attacker, String defender) throws IOException, ClassNotFoundException, GameNotFoundException {
        UUID gameId = UUID.fromString(gameIdString);
        Game game = GameControllerFacade.getInstance().getGameById(gameId);
        AttackResult ar = (AttackResult) reader.readUnshared();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                GUIControl.getInstance().fight(game.getCountries().get(attacker), game.getCountries().get(defender), ar);

            }
        });

        writer.writeUTF(GET_GAME + "," + gameIdString);
        writer.flush();
        Game fightFinishedGame = (Game) reader.readUnshared();
        GameControllerFacade.getInstance().setGame(fightFinishedGame);

        if (fightFinishedGame.getTurn().getPhase().equals(Phase.TRAIL_UNITS)) {
            writer.writeUTF(SET_TURN + "," + gameId + "," + Phase.TRAIL_UNITS.toString());
        } else {
            writer.writeUTF(SET_TURN + "," + gameId + "," + Phase.PERFORM_ANOTHER_ATTACK.toString());
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIControl.getInstance().getComponentMap().get(attacker + "info-hbox").update();
                GUIControl.getInstance().getComponentMap().get(defender + "info-hbox").update();

                GUIControl.getInstance().getComponentMap().get("dialog-vbox").update();
            }
        });
        writer.flush();
    }

    private void move(String gameId, String srcCountry, String destCountry, String trail) throws IOException, ClassNotFoundException {
        writer.writeUTF(GET_GAME + "," + gameId);
        writer.flush();
        Game moveGame = (Game) reader.readUnshared();
        GameControllerFacade.getInstance().setGame(moveGame);


        GUIControl.getInstance().getComponentMap().get(srcCountry + "info-hbox").update();
        GUIControl.getInstance().getComponentMap().get(destCountry + "info-hbox").update();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                GUIControl.getInstance().getComponentMap().get("dialog-vbox").update();
            }
        });
    }

    private void error() throws IOException, ClassNotFoundException {
        Exception e = (Exception) reader.readUnshared();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new ErrorAlert(e);
            }
        });

    }

}

