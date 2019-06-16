package server;

import controller.GameController;
import exceptions.*;
import model.Game;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import static helper.Events.*;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Socket connected!");
        InputStream is;
        ObjectInputStream ois;
        OutputStream os;
        ObjectOutputStream oos;


        try {
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);

            ServerManager.getInstance().getSocketObjectOutputStreamMap().put(socket, oos);
            ServerManager.getInstance().getSocketObjectInputStreamMap().put(socket, ois);


        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String line;

        while (true) {
            try {
                line = ois.readUTF();

                //TODO: SPLIT line to format;
                //MARK: Exception werfen?
                if ((line == null) || line.equalsIgnoreCase("quit")) {
                    socket.close();
                } else {
                    String split[] = line.split(",");
                    if (split[0].equals(CREATE_GAME)) {
                        GameController.getInstance().createGameRoom(UUID.fromString(split[1]), split[2], socket);
                        System.out.println("Room created: " + split[1]);
                    } else if (split[0].equals(PLAYER_JOIN)) {
                        UUID gameId = UUID.fromString(split[1]);
                        String playerName = split[2];
                        System.out.println("player joined: " + playerName);
                        ServerManager.getInstance().addPlayer(gameId, playerName, socket);
                        StringBuilder sb = new StringBuilder();
                        List<String> playerList = ServerManager.getInstance().getGameInitById(gameId).getPlayerList();
                        for (String s : playerList) {
                            sb.append(s);
                            if (!(playerList.indexOf(s) == playerList.size() - 1)) {
                                sb.append(",");
                            }
                        }

                        for (Socket s : ServerManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = ServerManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(PLAYER_JOIN + "," + sb.toString());
                            sOos.flush();
                        }
                    } else if (split[0].equals(START_GAME)) {
                        System.out.println("Game Starts...");

                        UUID gameId = UUID.fromString(split[1]);
                        for (Socket s : ServerManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = ServerManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(START_GAME);
                            sOos.flush();
                        }
                        GameController.getInstance().initNewGame(gameId);
                        List<Socket> gameSockets = ServerManager.getInstance().getGameInitById(gameId).getSockets();
                        ServerManager.getInstance().getGameIdSocketMap().put(gameId, gameSockets);

                        for (Socket s : ServerManager.getInstance().getGameIdSocketMap().get(gameId)) {

                            Game game = GameController.getInstance().getGameById(gameId);
                            ObjectOutputStream sOos = ServerManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeObject(game);
                             sOos.flush();
                        }

                    } else if (split[0].equals(GET_GAME)) {
                        UUID gameId = UUID.fromString(split[1]);
                        Game game = GameController.getInstance().getGameById(gameId);
                        ObjectOutputStream sOos = ServerManager.getInstance().getSocketObjectOutputStreamMap().get(socket);
                        sOos.writeObject(game);
                        sOos.flush();

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

            } catch (GameNotFoundException |  CountriesAlreadyAssignedException | InvalidFormattedDataException | MaximumNumberOfPlayersReachedException | InvalidPlayerNameException | NoSuchPlayerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                try {
                    socket.close();
                    break;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
