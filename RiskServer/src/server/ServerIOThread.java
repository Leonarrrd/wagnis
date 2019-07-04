package server;

import controller.GameController;
import datastructures.Phase;
import exceptions.*;
import helper.GameInit;
import javafx.scene.Scene;
import model.AttackResult;
import model.Country;
import model.Game;
import model.Player;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import static datastructures.Phase.TRAIL_UNITS;
import static helper.Events.*;

/**
 * ServerIOThread is a thread active for every socket that reads input
 * and sends responses to certain sockets depending on the input
 *
 *
 */
public class ServerIOThread extends Thread {
    private Socket socket;
    private GameController gc = GameController.getInstance();

    public ServerIOThread(Socket socket) {
        this.socket = socket;

    }

    private InputStream is;
    private ObjectInputStream ois;
    private OutputStream os;
    private ObjectOutputStream oos;

    /**
     * Waits for string input from the clients and passes it onto other methods that will
     * parse the string and perform the operations needed
     */
    public void run() {
        System.out.println("Socket connected!");
        try {

            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);

            SocketGameManager.getInstance().getSocketObjectOutputStreamMap().put(socket, oos);

            String clientInput;

            while (true) {
                clientInput = ois.readUTF();
                String split[] = clientInput.split(",");
                //split[0] should always be the event name
                //split[1] should always be the game Id
                String event = split[0];
                System.out.println(event);

                UUID gameId = null;
                if (!split[0].equals(LOAD_AVAILABLE_GAME_IDS)) {
                    gameId = UUID.fromString(split[1]);
                }

                Game game = null;
                Country country = null;
                switch (event) {
                    case CREATE_GAME:
                        // split[2]  playerName
                        createGame(gameId, split[2]);
                        System.out.println("Room created: " + gameId.toString());
                        break;
                    case LOAD_AVAILABLE_GAME_IDS:
                        loadAvailableGameIds();
                        break;
                    case CHECK_GAME_TYPE:
                        checkGameType(gameId);
                        break;
                    case PLAYER_JOIN:
                        //split[2] playerName
                        playerJoin(gameId, split[2]);
                        break;
                    case START_GAME:
                        System.out.println("Game Starts...");
                        startGame(gameId);
                        break;
                    case START_LOADED_GAME:
                        System.out.println("Game Starts...");
                        startLoadedGame(gameId);
                        break;
                    case LOAD_GAME:
                        loadGame(gameId);
                        break;
                    case SAVE_GAME:
                        saveGame(gameId);
                        System.out.println("Game saved: " + gameId.toString());
                        break;
                    case GET_GAME:
                        getGame(gameId);
                        break;
                    case SWITCH_TURNS:
                        // split[2] notifyAll flag
                        switchTurns(gameId, split[2]);
                        break;
                    case SET_TURN:
                        // split[2] phase
                        setTurn(gameId, split[2]);
                        break;
                    case USE_CARDS:
                        //split[2] playerName
                        //split[3] infantryCards
                        //split[4] cavalryCards
                        //split[5] artilleryCards
                        useCards(gameId, split[2], split[3], split[4], split[5]);
                        break;
                    case CHANGE_UNITS:
                        //split[2] should be country name
                        //split[3] should be the units
                        changeUnits(gameId, split[2], split[3]);
                        break;
                    case INIT_ATTACK:
                        //split[2]: attacking country name
                        //split[3]: defending country name
                        //split[4]:units
                        initAttack(gameId, split[2], split[3], split[4]);
                        break;
                    case FIGHT:
                        //split[2]: attacking country name
                        //split[3]: defending country name
                        //split[4]:attackingUnits
                        //split[5]:defendingUnits
                        fight(gameId, split[2], split[3], split[4], split[5]);
                        break;
                    case MOVE:
                        // split[2] sourceCountry
                        // split[3] destCountry
                        // split[4] unitsString
                        // split[5] trail
                        move(gameId, split[2], split[3], split[4], split[5]);
                        break;
                }
            }
        } catch (InvalidPlayerNameException | MaximumNumberOfPlayersReachedException e) {
            // Exceptions that can occur in the lobby have to be handled differently
            // than ingame exceptions
            // socket will not be reset if exception occurs in the lobby
            // if an exception occurs ingame and an ERROR event can't be sent to the client, his socket will be closed
            try {
                oos.writeUTF(ERROR);
                oos.flush();
                oos.writeUnshared(e);
                oos.flush();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (NoSuchCardException |  CountriesNotConnectedException
                | GameNotFoundException | NoSuchPlayerException | NoSuchCountryException
                |  CountriesNotAdjacentException | CardAlreadyOwnedException | NotEnoughUnitsException
                | CountriesAlreadyAssignedException | CountryNotOwnedException | DuplicateGameIdException
                | InvalidFormattedDataException e) {
            e.printStackTrace();
            try {
                oos.writeUTF(ERROR);
                oos.flush();
                oos.writeUnshared(e);
                oos.flush();
                oos.reset();
            } catch (IOException e1) {
                e1.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void createGame(UUID gameId, String hostPlayerName) throws InvalidPlayerNameException {
        GameController.getInstance().createGameRoom(gameId, hostPlayerName, socket);
    }

    private void loadAvailableGameIds() throws IOException {
        List<String> gameIdsList = null;
        gameIdsList = gc.loadAvailableGameIds();
        StringBuilder sb1 = new StringBuilder();
        for (String ID : gameIdsList) {
            sb1.append(ID);
            if (!ID.equals(gameIdsList.get(gameIdsList.size() - 1))) {
                sb1.append(",");
            }
        }
        oos.writeUTF(LOAD_AVAILABLE_GAME_IDS + "," + sb1.toString());
        oos.flush();
    }

    private void checkGameType(UUID gameId) throws IOException {
        String type = "invalid";
        StringBuilder sb = new StringBuilder();
        if (gc.getActiveGames().containsKey(gameId )) {
            for (Player player : gc.getActiveGames().get(gameId).getPlayers()){
                if (!SocketGameManager.getInstance().getSocketPlayerNameMap().containsValue(player.getName())) {
                    sb.append(player.getName());
                    sb.append(",");
                }
            }
            type = "loadedGame";
        } else {
            for (GameInit gi : SocketGameManager.getInstance().getGameInitList()) {
                if (gi.getGameId().equals(gameId)) {
                    type = "newGame";
                }
            }
        }
        oos.writeUTF(CHECK_GAME_TYPE + "," + gameId.toString() + "," + type + "," + sb.toString());
        oos.flush();
    }

    private void playerJoin(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException, IOException {
        System.out.println("player joined: " + playerName);

        SocketGameManager.getInstance().addPlayer(gameId, playerName, socket);

        StringBuilder sb2 = new StringBuilder();
        List<String> playerList = SocketGameManager.getInstance().getGameInitById(gameId).getPlayerList();
        for (String s : playerList) {
            sb2.append(s);
            if (!(playerList.indexOf(s) == playerList.size() - 1)) {
                sb2.append(",");
            }
        }

        for (Socket s : SocketGameManager.getInstance().getGameInitById(gameId).getSockets()) {
            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
            sOos.writeUTF(PLAYER_JOIN + "," + sb2.toString());
            sOos.flush();
        }
    }

    private void startGame(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException, InvalidFormattedDataException, NoSuchCardException, IOException, NoSuchPlayerException, CardAlreadyOwnedException, InvalidPlayerNameException, MaximumNumberOfPlayersReachedException {
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
        SocketGameManager.getInstance().removeGameInit(gameId);
    }

    private void startLoadedGame(UUID gameId) throws IOException, GameNotFoundException {
        List<Socket> gameSockets1 = SocketGameManager.getInstance().getGameInitById(gameId).getSockets();
        SocketGameManager.getInstance().getGameIdSocketMap().put(gameId, gameSockets1);

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
    }

    private void loadGame(UUID gameId) throws NoSuchPlayerException, InvalidFormattedDataException, GameNotFoundException, IOException {
        GameController.getInstance().loadGame(gameId);
        System.out.println("Room created: " + gameId.toString());
        oos.writeUTF(GET_GAME + "," + gameId);
        oos.flush();
    }

    private void saveGame(UUID gameId) throws GameNotFoundException, DuplicateGameIdException, IOException {
        gc.saveGame(gameId);
    }

    private void getGame(UUID gameId) throws IOException, GameNotFoundException {
        Game game = GameController.getInstance().getGameById(gameId);
        oos.writeUnshared(game);
        oos.flush();
        oos.reset();
    }

    private void switchTurns(UUID gameId, String notifyAllFlag) throws NoSuchPlayerException, CardAlreadyOwnedException, NoSuchCardException, IOException, GameNotFoundException {
        gc.switchTurns(gameId);
        //Making sure that all Sockets get the SWITCH_TURNS
        //There are cases where the sending socket needs to know and cases where all sockets need to know
        //thats what split[2] = "notifyall" is there for
        if (notifyAllFlag.equals("doNotifyAll")) {
            for (Socket s : SocketGameManager.getInstance().getGameIdSocketMap().get(gameId)) {
                ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                sOos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
                sOos.flush();
            }
        } else {
            oos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
            oos.flush();
        }
    }

    private void setTurn(UUID gameId, String phase) throws NoSuchPlayerException, NoSuchCardException, IOException, CardAlreadyOwnedException, GameNotFoundException {
        gc.setTurn(gameId, Phase.valueOf(phase));
        oos.writeUTF(SWITCH_TURNS + "," + gameId.toString());
        oos.flush();
    }

    private void useCards(UUID gameId, String playerName, String infantryCardsString, String cavalryCardsString, String artilleryCardsString) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException {
        Game game = gc.getGameById(gameId);
        Player player = null;
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                player = p;
            }
        }
        int infantryCards = Integer.parseInt(infantryCardsString);
        int cavalryCards = Integer.parseInt(cavalryCardsString);
        int artilleryCards = Integer.parseInt(artilleryCardsString);
        gc.useCards(gameId, player, infantryCards, cavalryCards, artilleryCards);
    }

    private void changeUnits(UUID gameId, String countryString, String unitsString) throws NoSuchCountryException, GameNotFoundException, NoSuchPlayerException, IOException {
        Game game = GameController.getInstance().getGameById(gameId);

        int units = Integer.parseInt(unitsString);
        Country country = game.getCountries().get(countryString);
        GameController.getInstance().changeUnits(gameId, country, units);
        GameController.getInstance().changeUnitsToPlace(gameId, country.getOwner(), -units);
        for (Socket s : SocketGameManager.getInstance().getGameIdSocketMap().get(gameId)) {
            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
            sOos.writeUTF(CHANGE_UNITS + "," + gameId.toString() + "," + countryString);
            sOos.flush();
        }
    }

    private void initAttack(UUID gameId, String attackingCountryString, String defendingCountryString, String unitsString) throws GameNotFoundException, IOException {
        Game game = GameController.getInstance().getGameById(gameId);
        Country attackingCountry = game.getCountries().get(attackingCountryString);
        Country defendingCountry = game.getCountries().get(defendingCountryString);
        int units = Integer.parseInt(unitsString);

        for (Socket s : SocketGameManager.getInstance().getGameIdSocketMap().get(gameId)) {
            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
            sOos.writeUTF(DEFENSE + "," + attackingCountry + "," + defendingCountry + "," + units);
            sOos.flush();
        }
    }

    private void fight(UUID gameId, String attackingCountryString, String defendingCountryString, String attackingUnitsString, String defendingUnitsString) throws GameNotFoundException, NoSuchCountryException, CountriesNotAdjacentException, NotEnoughUnitsException, NoSuchPlayerException, NoSuchCardException, IOException, CardAlreadyOwnedException {
        Game game = gc.getGameById(gameId);
        Country fightAttackingCountry = game.getCountries().get(attackingCountryString);
        Country fightDefendingCountry = game.getCountries().get(defendingCountryString);
        int fightAttackingUnits = Integer.parseInt(attackingUnitsString);
        int fightDefendingUnits = Integer.parseInt(defendingUnitsString);
        AttackResult ar = gc.fight(gameId, fightAttackingCountry, fightDefendingCountry, fightAttackingUnits, fightDefendingUnits);

        if (ar.getWinner() != null) {
            if (ar.getWinner().equals(fightAttackingCountry)) {
                updatePlayerGraph(game);
                gc.setTurn(gameId, TRAIL_UNITS);
            }
        }

        for (Socket s : SocketGameManager.getInstance().getGameIdSocketMap().get(gameId)) {
            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
            sOos.writeUTF(FIGHT_FINISHED + "," + gameId.toString() + ","
                    + fightAttackingCountry.getName() + "," + fightDefendingCountry.getName());
            sOos.flush();
            sOos.writeUnshared(ar);
            sOos.flush();
        }
    }

    private void move(UUID gameId, String srcCountry, String destCountry, String unitsString, String trail) throws GameNotFoundException, NoSuchPlayerException, IOException, CardAlreadyOwnedException, NoSuchCardException, CountriesNotConnectedException, CountryNotOwnedException, NotEnoughUnitsException, NoSuchCountryException {
        Game game = gc.getGameById(gameId);
        Country c1 = game.getCountries().get(srcCountry); // E noSuchCountry
        Country c2 = game.getCountries().get(destCountry); // E noSuchCountry
        int units = Integer.parseInt(unitsString); // E?

        if (trail.equals("trail")) {
            gc.setTurn(gameId, Phase.PERFORM_ANOTHER_ATTACK);

        } else {
            gc.setTurn(gameId, Phase.PERFORM_ANOTHER_MOVE);
        }

        gc.moveUnits(gameId, c1, c2, units, false); // E
        for (Socket s : SocketGameManager.getInstance().getGameIdSocketMap().get(gameId)) {
            ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
            sOos.writeUTF(MOVE + "," + gameId.toString() + "," + c1.getName() + "," + c2.getName() + "," + trail);
            sOos.flush();
        }
    }

    public void removePlayer() throws IOException {
        oos.reset();
        oos.flush();
        oos.writeUTF(REMOVE_PLAYER);
    }

    public void endGame(String playerString) throws IOException {
        oos.reset();
        oos.flush();
        oos.writeUTF(END_GAME + "," + playerString);
        oos.flush();
    }

    /**
     * Helper-Method that updates the Movement-Graph for each player
     *
     * @param game
     * @throws NoSuchPlayerException if there is no such player for this Graph
     * @throws GameNotFoundException if the does not exist
     */
    private void updatePlayerGraph(Game game) throws NoSuchPlayerException, GameNotFoundException {
        for (Player p : game.getPlayers()) {
            GameController.getInstance().updatePlayerGraphMap(game.getId(), p);
        }
    }



}
