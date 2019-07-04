package view.gui.helper;

import datastructures.Phase;
import exceptions.*;
import interfaces.IGameController;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import model.AttackResult;
import model.Country;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.LogHBox;
import view.gui.panes.DiceGridPane;
import view.gui.roots.GameBorderPane;
import view.gui.roots.StartBorderPane;
import view.gui.sockets.GameControllerFacade;
import view.gui.viewhelper.CountryViewHelper;
import view.gui.viewhelper.LastFightCountries;

import java.io.IOException;
import java.util.*;


public class GUIControl {

    private static GUIControl instance;
    private Map<String, Updatable> componentMap = new HashMap<>();
    private UUID gameId;
    private Map<String, CountryViewHelper> countryViewMap = CountryRelationLoadHelper.buildCountryViewMap();
    private IGameController gc = GameControllerFacade.getInstance();
    private String selectedCountry = "Brazil";
    private LastFightCountries lastFightCountry;

    private List<String> playersInLobby = new ArrayList();

    /**
     * return the game by the provieded Id
     *
     * @return
     */
    public Game getGame() {
        try {
            return gc.getGameById(gameId);
        } catch (GameNotFoundException | IOException | ClassNotFoundException e) {
            new ErrorAlert(e);
        }
        return null;
    }

    private GUIControl() {
    }

    public static GUIControl getInstance() {
        if (instance == null) {
            instance = new GUIControl();
        }
        return instance;
    }

    /**
     * updates the lobby grid pane
     * needs to be called when a player joined
     */
    public void playerJoined() {
        componentMap.get("start-new-game-grid-pane").update();
    }

    /**
     * switches from the lobby to the game scene
     * needs to be called when a game is being started
     *
     * @param gameId
     */
    public void switchToGameScene(UUID gameId) {
        this.gameId = gameId;

        //FIXME: workaround to access the scene
        ((GridPane) componentMap.get("start-new-game-grid-pane")).getScene().setRoot(new GameBorderPane());
    }

    /**
     * Sends the request to the Facade to use cards and forwards the phase
     *
     * @param infantryCards
     * @param cavalryCards
     * @param artilleryCards
     */
    public void useCards(int infantryCards, int cavalryCards, int artilleryCards) {
        try {
            gc.useCards(gameId, getCurrentPlayer(), infantryCards, cavalryCards, artilleryCards);
            //FIXME: Server needs time to process
            // this solution is very hacky, needs to be fixed, because the server
            // can be slower than the client wait time.

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                new ErrorAlert(e);
            }
            forwardTurnPhase();

        } catch (GameNotFoundException | NoSuchCardException | NoSuchPlayerException | IOException e) {
            new ErrorAlert(e);
        }
    }

    /**
     * Sends the request to the Facade and places the units
     *
     * @param units
     * @throws GameNotFoundException
     * @throws NoSuchCountryException
     * @throws IOException
     */
    public void placeUnits(int units) throws GameNotFoundException, NoSuchCountryException, IOException {
        Country c = getGame().getCountries().get(selectedCountry);
        gc.changeUnits(gameId, c, units);
        getLog().update(c.getOwner() + " placed " + units + " units on " + c.getName());

        //FIXME: Server needs time to process
        // this solution is very hacky, needs to be fixed, because the server
        // can be slower than the client wait time.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            new ErrorAlert(e);
        }

        if (getGame().getTurn().getPlayer().getUnitsToPlace() <= 0) {
            forwardTurnPhase();
        } else {
            // only this players GUI needs to be updated
            componentMap.get("dialog-vbox").update();
        }
    }

    /**
     * Tell the server to start an attack with given parameters
     *
     * @param attackingCountry
     * @param defendingCountry
     * @param units
     * @throws GameNotFoundException
     * @throws NoSuchCountryException
     * @throws IOException
     */
    public void initAttack(String attackingCountry, String defendingCountry, int units) throws GameNotFoundException, NoSuchCountryException, IOException {
        forwardTurnPhase();

        //FIXME: Server needs time to process
        // this solution is very hacky, needs to be fixed, because the server
        // can be slower than the client wait time.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            new ErrorAlert(e);
        }
        gc.initAttack(gameId, attackingCountry, defendingCountry, units);
    }

    /**
     * Excecutes the fight and updates GUI components accordingly
     *
     * @param attackingCountry
     * @param defendingCountry
     * @param ar
     */
    public void fight(Country attackingCountry, Country defendingCountry, AttackResult ar) {

        componentMap.get(attackingCountry + "info-hbox").update();
        componentMap.get(defendingCountry + "info-hbox").update();


        getLog().update("Attacking Player: " + attackingCountry.getOwner() + " with country: " + attackingCountry + " rolled: ");
        for (int attDices : ar.getAttackerDices()) {
            getLog().update(attDices + "");
        }
        getLog().update("Defending Player: " + defendingCountry.getOwner() + " with country: " + defendingCountry + " rolled: ");
        for (int defDices : ar.getDefenderDices()) {
            getLog().update(defDices + "");
        }

        ((DiceGridPane) componentMap.get("dice-grid")).update(attackingCountry.getOwner().getColor().toString(), defendingCountry.getOwner().getColor().toString(), ar.getAttackerDices(), ar.getDefenderDices());

        if (ar.getWinner() != null) {
            if (ar.getWinner().equals(defendingCountry)) {
                getLog().update(defendingCountry + " successfully defended. It is owned by: " + defendingCountry.getOwner());
            } else {
                lastFightCountry = new LastFightCountries(attackingCountry, defendingCountry);
                getLog().update(defendingCountry + " successfully attacked. It is now owned by: " + defendingCountry.getOwner());

            }
        } else {
            return;
        }


    }

    /**
     * Sends the request to trail units to the facade an updates the GUI-Elements accordingly
     *
     * @param value
     */
    public void trailUnits(int value) {
        try {

            gc.moveUnits(gameId, lastFightCountry.getSrcCountry(), lastFightCountry.getDestCountry(), value, true);
            componentMap.get(lastFightCountry.getSrcCountry() + "info-hbox").update();
            componentMap.get(lastFightCountry.getDestCountry() + "info-hbox").update();

        } catch (IOException | CountriesNotConnectedException | GameNotFoundException | NotEnoughUnitsException | NoSuchCountryException | CountryNotOwnedException e) {
            new ErrorAlert(e);
        }
    }

    /**
     * Send the request to move units to the facade with given parameters
     *
     * @param srcCountry
     * @param destCountry
     * @param amount
     */
    public void move(String srcCountry, String destCountry, int amount) {
        Country srcCountryObj = getCountryFromString(srcCountry);
        Country destCountryObj = getCountryFromString(destCountry);
        try {
            gc.moveUnits(getGame().getId(), srcCountryObj, destCountryObj, amount, false);
            getLog().update(srcCountryObj.getOwner().getName() + " moved " + amount + " from " + srcCountry + " to " + destCountry);

        } catch (IOException | CountriesNotConnectedException | GameNotFoundException | NotEnoughUnitsException | CountryNotOwnedException | NoSuchCountryException e) {
            new ErrorAlert(e);
        }
    }

    /**
     * Sends the request to switch the turns to the Facade
     */
    public void forwardTurnPhase() {
        try {
            GameControllerFacade.getInstance().switchTurns(gameId, false);
        } catch (GameNotFoundException | IOException | NoSuchCardException | NoSuchPlayerException | CardAlreadyOwnedException e) {
            new ErrorAlert(e);
        }
    }

    /**
     * Sends the request to switch the turns to the Facade and notifies all players (server side logic)
     */
    public void nextPlayerTurn() {
        try {
            GameControllerFacade.getInstance().switchTurns(gameId, true);
        } catch (GameNotFoundException | NoSuchPlayerException | NoSuchCardException | CardAlreadyOwnedException | IOException e) {
            new ErrorAlert(e);
        }

    }

    public void setTurnManually(Phase phase) throws NoSuchPlayerException, GameNotFoundException, IOException, NoSuchCardException, CardAlreadyOwnedException {
        gc.setTurn(gameId, phase);

    }

    /**
     * method called when a country is clicked
     *
     * @param
     */
    public void countryClicked(String countryString) throws GameNotFoundException, IOException, ClassNotFoundException {
        if (myTurn()) {
            selectedCountry = countryString;
            Game game = getGame();
            switch (game.getTurn().getPhase()) {
                case ATTACK:
                    Updatable attackVBox = componentMap.get("attack-vbox");
                    attackVBox.update();
                    break;
                case MOVE:
                    Updatable moveVBox = componentMap.get("move-vbox");
                    moveVBox.update();
                    break;
                case PLACE_UNITS:
                    Updatable placeUnitsVBox = componentMap.get("place-units-vbox");
                    placeUnitsVBox.update();
                    break;
            }
        }
    }

    /**
     * Looks up a the corresponding Country to a color Code
     * IMPORTANT: the caller of this method needs to handle the case that the player did not click on a country (nothing should happen then)
     * MARK: Maybe throw an exception instead of returning null
     *
     * @param colorCode
     * @return countryString if found, empty string if no country has been found for the param colorCode
     */
    public String getCountryStringFromColorCode(String colorCode) {
        for (String countryString : countryViewMap.keySet()) {
            if (countryViewMap.get(countryString).getColorCode().equals(colorCode)) {
                return countryString;
            }
        }
        return null;
    }

    public Map<String, CountryViewHelper> getCountryViewMap() {
        return countryViewMap;
    }

    /**
     * gets the country object from param country name
     *
     * @param countryString
     * @return
     */
    public Country getCountryFromString(String countryString) {
        try {
            return gc.getGameById(gameId).getCountries().get(countryString);
        } catch (IOException | ClassNotFoundException | GameNotFoundException e) {
            new ErrorAlert(e);
            return null;
        }
    }

    /**
     * Returns to lobby when the player has lost or another player has won
     * @param lost
     */
    public void returnToLobby(boolean lost) {
        String message = "";
        if (lost) {
            message = "You lost.";
        } else {
            message = "You won!!";
        }
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
        ((Node) componentMap.get("log-hbox")).getScene().setRoot(new StartBorderPane());
    }

    /**
     * get the player from this client
     * @return
     */
    public Player getThisPlayer() {
        for (Player player : getGame().getPlayers()) {
            if (player.getName().equals(getPlayerName()))
                return player;
        }
        return null;
    }


    // Mark: methods below to verify input locally, rather than requesting verification from the server
    // since the server itself later verifies if the operation is legal,
    // doing the input verification locally should be fine

    public boolean hasHostileNeighbors(Country country) {
        for (Country neighbor : country.getNeighbors()) {
            if (!country.getOwner().equals(neighbor.getOwner()))
                return true;
        }
        return false;
    }

    public boolean isLegalCardUse(int iCards, int cCards, int aCards) {
        if (iCards + cCards + aCards != 3) {
            return false;
        }
        if (iCards == 3 || cCards == 3 || aCards == 3) {
            return true;
        }
        if (iCards == 1 & cCards == 1 & aCards == 1) {
            return true;
        }
        return false;
    }

    public boolean myTurn() throws GameNotFoundException, IOException, ClassNotFoundException {
        if (gc.getGameById(gameId) != null) {
            return GameControllerFacade.getInstance().getPlayerName().equals(getGame().getTurn().getPlayer().getName());
        } else {
            return false;
        }
    }

    //MARK: end


    public Country getSelectedCountry() {
        return getGame().getCountries().get(selectedCountry);
    }

    public Player getCurrentPlayer() {
        return getGame().getTurn().getPlayer();
    }

    public String getPlayerName() {
        return GameControllerFacade.getInstance().getPlayerName();
    }

    public LogHBox getLog() {
        return (LogHBox) componentMap.get("log-hbox");
    }

    public LastFightCountries getLastFightCountry() {
        return lastFightCountry;
    }

    public void saveGame() throws GameNotFoundException, DuplicateGameIdException, IOException {
        gc.saveGame(gameId);
        getLog().update("Game has been saved successfully");
    }

    public void setPlayersInLobby(List<String> playersInLobby) {
        this.playersInLobby = playersInLobby;
    }

    public List<String> getPlayersInLobby() {
        return playersInLobby;
    }

    public Map<String, Updatable> getComponentMap() {
        return componentMap;
    }
}
