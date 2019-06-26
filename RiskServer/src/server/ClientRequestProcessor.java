package server;

import controller.GameController;
import controller.GraphController;
import datastructures.Color;
import datastructures.Phase;
import exceptions.*;
import model.AttackResult;
import model.Country;
import model.Game;
import model.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static helper.Events.*;
import static helper.Events.CHANGE_UNITS;

public class ClientRequestProcessor extends Thread {
    private Socket socket;
    private GameController gc = GameController.getInstance();

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
                Country country = null;
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
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(GET_GAME + "," + gameId);
                            sOos.flush();

                        }
                        break;
                    case GET_GAME:
                        game = GameController.getInstance().getGameById(gameId);
                        oos.writeUnshared(game);
                        oos.flush();
                        oos.reset();
                        break;

                    case SWITCH_TURNS:
                        gc.switchTurns(gameId);
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
                            sOos.flush();
                        }
                        break;
                    case SET_TURN:
                        gc.setTurn(gameId, Phase.valueOf(split[2]));
                        oos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
                        oos.flush();
                        /*for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
                            sOos.flush();
                        }*/
                        break;
                    case USE_CARDS:
                        //split[2] playerName
                        //split[3] infantryCards
                        //split[4] cavalryCards
                        //split[5] artilleryCards
                        game = gc.getGameById(gameId);
                        Player player = null;
                        for (Player p : game.getPlayers()) {
                            if (p.getName().equals(split[2])) {
                                player = p;
                            }
                        }
                        int infantryCards = Integer.parseInt(split[3]);
                        int cavalryCards = Integer.parseInt(split[4]);
                        int artilleryCards = Integer.parseInt(split[5]);
                        gc.useCards(gameId, player, infantryCards, cavalryCards, artilleryCards);
                        break;
                    case CHANGE_UNITS:
                        //split[2] should be country name
                        //split[3] should be the units
                        game = GameController.getInstance().getGameById(gameId);

                        String countryString = split[2];
                        int units = Integer.parseInt(split[3]);
                        country = game.getCountries().get(countryString);
                        GameController.getInstance().changeUnits(gameId, country, units);
                        GameController.getInstance().changeUnitsToPlace(gameId, country.getOwner(), -units);
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(CHANGE_UNITS + "," + gameId.toString() + "," + countryString);
                            sOos.flush();
                        }
                        break;
                    case INIT_ATTACK:
                        game = GameController.getInstance().getGameById(gameId);
                        //split[2]: attacking country name
                        //split[3]: defending country name
                        //split[4]:units
                        Country attackingCountry = game.getCountries().get(split[2]);
                        Country defendingCountry = game.getCountries().get(split[3]);
                        units = Integer.parseInt(split[4]);


                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(DEFENSE + "," + gameId.toString() + "," + attackingCountry + "," + defendingCountry + "," + units);
                            sOos.flush();
                        }
                        break;
                    case FIGHT:
                        game = gc.getGameById(gameId);
                        //split[2]: attacking country name
                        //split[3]: defending country name
                        //split[4]:attackingUnits
                        //split[5]:defendingUnits
                        Country fightAttackingCountry = game.getCountries().get(split[2]);
                        Country fightDefendingCountry = game.getCountries().get(split[3]);
                        int fightAttackingUnits = Integer.parseInt(split[4]);
                        int fightDefendingUnits = Integer.parseInt(split[5]);
                        AttackResult ar = gc.fight(gameId, fightAttackingCountry, fightDefendingCountry, fightAttackingUnits, fightDefendingUnits);
                        updatePlayerGraph(game);

                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(FIGHT_FINISHED + "," + gameId.toString() + ","
                                    + fightAttackingCountry.getName() + "," + fightDefendingCountry.getName() + ",");
                            sOos.flush();
                            sOos.writeUnshared(ar);
                            sOos.flush();

                        }
                        break;
                    case MOVE:
                        game = gc.getGameById(gameId);
                        if (!game.getCountries().keySet().contains(split[2])) {
//                            throw new NoSuchCountryException(split[2]);
                        } else if (!game.getCountries().keySet().contains(split[3])) {
//                            throw new NoSuchCountryException(split[3]);
                        }

                        Country c1 = game.getCountries().get(split[2]); // E noSuchCountry
                        Country c2 = game.getCountries().get(split[3]); // E noSuchCountry
                        units = Integer.parseInt(split[4]); // E?

                        if (!c1.getOwner().getCountryGraph().isConnected(c1, c2)) {
                            throw new CountriesNotConnectedException(c1, c2);
                        }
                        gc.moveUnits(gameId, c1, c2, units); // E
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(MOVE + "," + gameId.toString() + "," + c1.getName() + "," + c2.getName());
                            sOos.flush();
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
        } catch (InvalidPlayerNameException | NoSuchPlayerException | GameNotFoundException | CountriesAlreadyAssignedException | NoSuchCountryException | MaximumNumberOfPlayersReachedException | InvalidFormattedDataException | NoSuchCardException | CardAlreadyOwnedException | CountriesNotConnectedException | NotEnoughUnitsException | CountriesNotAdjacentException | CountryNotOwnedException e) {
            e.printStackTrace();
        }


    }

    private void updatePlayerGraph(Game game) throws NoSuchPlayerException, GameNotFoundException {
        for (Player p : game.getPlayers()) {
            GameController.getInstance().updatePlayerGraphMap(game.getId(), p);
        }
    }

}
