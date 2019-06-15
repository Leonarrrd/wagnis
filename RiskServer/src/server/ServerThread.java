package server;

import controller.GameController;
import exceptions.*;
import helpermodels.GameInit;
import model.Game;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.List;
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
                    String split[] = line.split(",");
                    if (split[0].equals("gamecreate")) {
                        GameController.getInstance().createGameRoom(UUID.fromString(split[1]), split[2], socket);
                        System.out.println("Room created: " + split[1]);
                    } else if (line.split(",")[0].equals("playerjoin")) {
                        UUID gameId = UUID.fromString(split[1]);
                        String playerName = split[2];
                        System.out.println("player joined: " + playerName);
                        ServerManager.getInstance().addPlayer(gameId, playerName, socket);
                        for(Socket s : ServerManager.getInstance().getGameInitById(gameId).getSockets()) {
                            PrintWriter pw = new PrintWriter(s.getOutputStream());
                            pw.println("playerjoin," + split[2]);
                            pw.flush();
                        }
                    } else if (line.split(",")[0].equals("gamestart")) {
                        System.out.println("Game Starts...");
                        UUID gameId = UUID.fromString(split[1]);
                        GameController.getInstance().initNewGame(gameId);
                        List<Socket> gameSockets = ServerManager.getInstance().getGameInitById(gameId).getSockets();
                        ServerManager.getInstance().getGameIdSocketMap().put(gameId, gameSockets);

                        for (Socket s : ServerManager.getInstance().getGameIdSocketMap().get(gameId)) {
                            PrintWriter pw = new PrintWriter(s.getOutputStream());
                            pw.println("hello to sockets in this game");
                            pw.flush();
                        }
                    } else {
                        //MARK: DO ALL THE GAME LOGIC HERE
                        UUID gameId = UUID.fromString(split[1]);
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
