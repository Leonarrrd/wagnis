package server;

import controller.GameController;
import controller.GraphController;
import datastructures.Color;
import datastructures.Phase;
import exceptions.*;
import model.AttackResult;
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
                            sOos.writeUTF(GET_GAME+ "," + gameId);
                            sOos.flush();
                            sOos.reset();
                            sOos.writeUnshared(GameController.getInstance().getGameById(gameId));
                            sOos.flush();
                            sOos.reset();
                        }
                        break;
                    case GET_GAME:
                        game = GameController.getInstance().getGameById(gameId);
                        oos.reset();
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
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
                            sOos.flush();
                        }
                        break;
                    case CHANGE_UNITS:
                        //split[2] should be country name
                        //split[3] should be the units
                        String countryString = split[2];
                        int units = Integer.parseInt(split[3]);
                        game = GameController.getInstance().getGameById(gameId);
                        country = game.getCountries().get(countryString);
                        GameController.getInstance().changeUnits(gameId, country, units);
                        GameController.getInstance().changeUnitsToPlace(gameId, country.getOwner(), -units);
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(CHANGE_UNITS+ "," + gameId.toString() + "," + countryString);
                            sOos.flush();
                        }
                        break;
                    case INIT_ATTACK:
                        game = GameController.getInstance().getGameById(gameId);
                        //split[2]: attacking country name
                        //split[3]: defending country name
                        //split[4]:units
                        // MARK: no need to check if countries + players exist, correct?
                        if (!(game.getCountries().keySet().contains(split[2]) && game.getCountries().keySet().contains(split[3]))){
                            // noSuchCountryException
                        }

                        Country attackingCountry = game.getCountries().get(split[2]);
                        Country defendingCountry = game.getCountries().get(split[3]);
                        units = Integer.parseInt(split[4]);


                        if (!attackingCountry.getOwner().equals(game.getTurn().getPlayer())){
                            // player doesn't own country
                        }
                        if (attackingCountry.getUnits() == 1){
                            // only one unit on country
                        }
                        if (units > attackingCountry.getUnits() - 1){
                            // not enough units on country
                        }
                        if (!attackingCountry.getNeighbors().contains(defendingCountry)){
                            // not neighbors
                        }
                        if (attackingCountry.getOwner().equals(defendingCountry.getOwner())){
                            // not hostile
                        }

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
                        // MARK: Actually, we need to do all the checks again here, right? + checking if defendingUnits are valid
                        Country fightAttackingCountry =  game.getCountries().get(split[2]);
                        Country fightDefendingCountry =  game.getCountries().get(split[3]);
                        int fightAttackingUnits =  Integer.parseInt(split[4]);
                        int fightDefendingUnits =  Integer.parseInt(split[5]);
                        AttackResult ar = gc.fight(gameId, fightAttackingCountry, fightDefendingCountry, fightAttackingUnits, fightDefendingUnits);
                        // FIXME!!!!: oof ouch owie, please ask me about this if i forget to
                        //  I think the easiest solution would be to just not have the winner in the attackresult
                        String winner = ar.getWinner() == null? "no_winner" : ar.getWinner().getName();
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(FIGHT_FINISHED + "," + gameId.toString() + ","
                                    + winner + "," + fightAttackingCountry.getName() + "," + fightDefendingCountry.getName() + ","
                                    + ar.getAttackerDices() + "," + ar.getDefenderDices());
                            sOos.flush();
                        }
                        break;
                    case MOVE:
                        game = gc.getGameById(gameId);
                        if (!game.getCountries().keySet().contains(split[2])){
//                            throw new NoSuchCountryException(split[2]);
                        } else if (!game.getCountries().keySet().contains(split[3])) {
//                            throw new NoSuchCountryException(split[3]);
                        }

                        Country c1 = game.getCountries().get(split[2]); // E noSuchCountry
                        Country c2 = game.getCountries().get(split[3]); // E noSuchCountry
                        units = Integer.parseInt(split[4]); // E?

                        if (!c1.getOwner().getCountryGraph().isConnected(c1, c2)){
                            throw new CountriesNotConnectedException(c1, c2);
                        }
                        gc.moveUnits(gameId, c1, c2, units); // E
                        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
                            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                            sOos.writeUTF(MOVE + "," + gameId.toString() + "," + c1.getName() + "," + c2.getName());
                            sOos.flush();
                        }
                        break;
//                    case HAS_COUNTRY_TO_ATTACK_FROM:
//                        gc.hasCountryToAttackFrom(gameId, gc.getGameById(gameId).getTurn().getPlayer());
//                        break;
//                    case HAS_COUNTRY_TO_MOVE_FROM:
//                        gc.hasCountryToMoveFrom(gameId, gc.getGameById(gameId).getTurn().getPlayer());
//                        break;
//                    case HAS_COUNTRY_TO_MOVE_TO:
//                        game = gc.getGameById(gameId);
//                        country = game.getCountries().get(split[2]);
//                        GraphController.getInstance().updatePlayerGraphMap(game, country.getOwner());
//                        boolean b = gc.hasCountryToMoveTo(gameId, country);
////                        oos.reset();
//                        oos.writeBoolean(b);
//                        oos.flush();
//                        break;
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
}
