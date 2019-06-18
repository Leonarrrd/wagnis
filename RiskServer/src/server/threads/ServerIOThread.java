package server.threads;

import controller.GameController;
import exceptions.*;
import model.Country;
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
    private ObjectOutputStream oos;

    public ServerIOThread(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
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
                System.out.println(event);

                UUID gameId = UUID.fromString(split[1]);

                Game game = null;

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


                        GameController.getInstance().initNewGame(gameId);
                        List<Socket> gameSockets = SocketGameManager.getInstance().getGameInitById(gameId).getSockets();
                        SocketGameManager.getInstance().getGameIdSocketMap().put(gameId, gameSockets);

                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);

                            sOos.writeUTF(START_GAME + "," + gameId);
                            sOos.flush();
                        }

                        break;
                    case GET_GAME:
                        game = GameController.getInstance().getGameById(gameId);
                        oos.writeObject(game);
                        oos.flush();

                        break;
                    case PLACE_UNITS:
                        //split[2] should be country name
                        //split[3] should be the units
                        String countryString = split[2];
                        int units = Integer.parseInt(split[3]);
                        game = GameController.getInstance().getGameById(gameId);
                        Country country = game.getCountries().get(countryString);

                        GameController.getInstance().changeUnits(gameId, country, units);
                        break;
                    default:
                        oos.flush();
                        break;
                }


            } catch (GameNotFoundException | CountriesAlreadyAssignedException | InvalidFormattedDataException | MaximumNumberOfPlayersReachedException | InvalidPlayerNameException | NoSuchPlayerException | NoSuchCountryException e) {
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
