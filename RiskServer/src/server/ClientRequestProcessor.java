package server;

import controller.GameController;
import exceptions.*;
import model.Country;
import model.Game;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import static helper.Events.*;
import static helper.Events.CHANGE_UNITS;

public class ClientRequestProcessor extends Thread {
    private Socket socket;

    public ClientRequestProcessor(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Socket connected!");
        try {

            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            SocketGameManager.getInstance().getSocketObjectOutputStreamMap().put(socket, oos);
            SocketGameManager.getInstance().getSocketObjectInputStreamMap().put(socket, ois);

            String clientInput;
            while (true) {
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
                            oos.writeObject(GameController.getInstance().getGameById(gameId));
                            oos.flush();
                            break;
                        case CHANGE_UNITS:
                            //split[2] should be country name
                            //split[3] should be the units
                            String countryString = split[2];
                            int units = Integer.parseInt(split[3]);
                            game = GameController.getInstance().getGameById(gameId);
                            Country country = game.getCountries().get(countryString);
                            GameController.getInstance().changeUnits(gameId, country, units);
                            GameController.getInstance().getGameById(gameId);
                            for (Socket s : SocketGameManager.getInstance().getGameIdSocketMap().get(gameId)) {
                                ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                                sOos.writeUTF(event + "," + gameId.toString() + "," + countryString);
                                sOos.flush();
                                sOos.writeObject(GameController.getInstance().getGameById(gameId));

                            }

                            break;
                        default:
                            break;
                    }



            }

        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        } catch (InvalidPlayerNameException | NoSuchPlayerException | GameNotFoundException | CountriesAlreadyAssignedException | NoSuchCountryException | MaximumNumberOfPlayersReachedException | InvalidFormattedDataException e) {
            e.printStackTrace();
        }


    }
}
