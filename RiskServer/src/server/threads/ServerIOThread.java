package server.threads;

import controller.GameController;
import exceptions.*;
import model.Game;
import server.SocketGameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import static helper.Events.*;

public class ServerIOThread extends Thread {
    private Socket socket;
    private ObjectInputStream ois;

    public ServerIOThread(Socket socket, ObjectInputStream ois) {
        this.socket = socket;
        this.ois = ois;
    }

    @Override
    public void run() {
        System.out.println("Server Lobby Thread started...");

        String clientInput;

        while (true) {
            try {
                clientInput = ois.readUTF();
                String split[] = clientInput.split(",");

                //split[0] should always be the event name
                //split[1] should always be the game Id
                String event = split[0];
                UUID gameId = UUID.fromString(split[1]);
                switch (event) {
                    case CREATE_GAME:
                        GameController.getInstance().createGameRoom(UUID.fromString(split[1]), split[2], socket);
                        System.out.println("Room created: " + split[1]);
                        break;
                    case PLAYER_JOIN:
                        String playerName = split[2];
                        System.out.println("player joined: " + playerName);
                        SocketGameManager.getInstance().addPlayer(gameId, playerName, socket);
                        StringBuilder sb = new StringBuilder();
                        List<String> playerList = SocketGameManager.getInstance().getGameInitById(gameId).getPlayerList();
                        for (String s : playerList) {
                            sb.append(s);
                            if (!(playerList.indexOf(s) == playerList.size() - 1)) {
                                sb.append(",");
                            }
                        }

                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(PLAYER_JOIN + "," + sb.toString());
                            sOos.flush();
                        }
                        break;
                    case START_GAME:
                        System.out.println("Game Starts...");

                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(START_GAME);
                            sOos.flush();
                        }
                        GameController.getInstance().initNewGame(gameId);
                        List<Socket> gameSockets = SocketGameManager.getInstance().getGameInitById(gameId).getSockets();
                        SocketGameManager.getInstance().getGameIdSocketMap().put(gameId, gameSockets);

                        for (Socket s : SocketGameManager.getInstance().getGameIdSocketMap().get(gameId)) {
                            Game game = GameController.getInstance().getGameById(gameId);
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);

                            sOos.writeObject(game);
                            sOos.flush();

                        }
                        break;
                    case GET_GAME:
                        Game game = GameController.getInstance().getGameById(gameId);
                        ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(socket);
                        sOos.writeObject(game);
                        sOos.flush();
                        break;

                }


            } catch (GameNotFoundException | CountriesAlreadyAssignedException | InvalidFormattedDataException | MaximumNumberOfPlayersReachedException | InvalidPlayerNameException | NoSuchPlayerException e) {
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
