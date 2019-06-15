package server;

import controller.GameController;
import exceptions.*;
import helpermodels.GameInit;
import model.Game;
import serverhelper.InitHelper;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.UUID;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Socket connected!");
        InputStream is = null;
        BufferedReader br = null;
        PrintWriter out = null;

        try {
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String line;
        while (true) {
            try {
                line = br.readLine();

                //TODO: SPLIT line to format;
                //MARK: Exception werfen?
                if ((line == null) || line.equalsIgnoreCase("quit")) {
                    socket.close();
                } else {
                    if (line.split(",")[0].equals("gamecreate")) {
                        GameController.getInstance().createGameRoom(UUID.fromString(line.split(",")[1]), line.split(",")[2]);
                        System.out.println("Room created: " + line.split(",")[1]);
                    } else if (line.split(",")[0].equals("gameinit")) {
                        System.out.println("player joined: " + line.split(",")[2]);
                        InitHelper.addPlayer(UUID.fromString(line.split(",")[1]), line.split(",")[2]);
                    } else if (line.split(",")[0].equals("gamestart")) {
                        System.out.println("Game Starts...");
                        GameController.getInstance().initNewGame(UUID.fromString(line.split(",")[1]));
                        out.println("hello");
                        out.flush();
                    }else {
                        //MARK: DO ALL THE GAME LOGIC HERE
                        UUID gameId = UUID.fromString("blabal");
                        Game game = GameController.getInstance().getGameById(gameId);
                        switch (game.getTurn().getPhase()) {
                            case USE_CARDS:
                                break;
                            case PLACE_UNITS:
                                break;
                            case ATTACK:
                                break;
                            case TRAIL_UNITS:
                                break;
                            case PERFORM_ANOTHER_ATTACK:
                                break;
                            case MOVE:
                                break;
                            case PERFORM_ANOTHER_MOVE:
                                break;
                            default:
                                break;
                        }
                    }
                }

            } catch (GameNotFoundException | IOException | CountriesAlreadyAssignedException | InvalidFormattedDataException | MaximumNumberOfPlayersReachedException | InvalidPlayerNameException | NoSuchPlayerException e) {
                e.printStackTrace();
            }
        }
    }
}
