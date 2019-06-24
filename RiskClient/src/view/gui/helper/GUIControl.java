package view.gui.helper;

import datastructures.Color;
import datastructures.Phase;
import exceptions.*;
import interfaces.IGameController;
import javafx.scene.layout.GridPane;
import model.AttackResult;
import model.Country;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.LogHBox;
import view.gui.panes.DiceGridPane;
import view.gui.roots.GameBorderPane;
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
    private String selectedCountry;
    private LastFightCountries lastFightCountry;

    private List<String> playersInLobby = new ArrayList();


    public Game getGame() {
        try {
            return gc.getGameById(gameId);
        } catch (GameNotFoundException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
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

    public void playerJoined() {
        componentMap.get("start-new-game-grid-pane").update();
    }

    public void initLoadedGame(UUID gameId) {
        try {
            gc.loadGame(gameId);
        } catch (GameNotFoundException| ClassNotFoundException | IOException | InvalidFormattedDataException e) {
            new ErrorAlert(e);
        }

        this.gameId = gameId;
        getGame().getPlayers().get(0).setColor(Color.BLUE);
        getGame().getPlayers().get(1).setColor(Color.RED);
        if (getGame().getPlayers().size() > 2) getGame().getPlayers().get(2).setColor(Color.GREEN);
        if (getGame().getPlayers().size() > 3) getGame().getPlayers().get(3).setColor(Color.YELLOW);
        if (getGame().getPlayers().size() > 4) getGame().getPlayers().get(4).setColor(Color.MACADAMIA);
        // MARK: FOR TESTING
        getGame().getTurn().getPlayer().setUnitsToPlace(7);
    }

    public void switchToGameScene(UUID gameId) {
        this.gameId = gameId;

        //FIXME:!!!! workaround to access the scene
        ((GridPane)componentMap.get("start-new-game-grid-pane")).getScene().setRoot(new GameBorderPane());
    }

    // TODO: still oversimplified
    public void useCards(int infantryCards, int cavalryCards, int artilleryCards){
        try {
            gc.useCards(gameId, getCurrentPlayer(), infantryCards, cavalryCards, artilleryCards);
        } catch (GameNotFoundException | NoSuchCardException | NoSuchPlayerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void placeUnits(int units) throws GameNotFoundException, NoSuchCountryException, IOException {
        Country c = getGame().getCountries().get(selectedCountry);
        gc.changeUnits(gameId, c, units);
        getLog().update(c.getOwner() + " placed " + units + " units on " + c.getName());

        // MARK: Need to wait client/server interaction
        //  this only occurs here, because at all other instances, we call forwardTurnPhase manually
        //  we could avoid this by also forwarding the turn in place units manually
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
     * @param attackingCountry
     * @param defendingCountry
     * @param units
     * @throws GameNotFoundException
     * @throws NoSuchCountryException
     * @throws IOException
     */
    public void initAttack(String attackingCountry, String defendingCountry, int units) throws GameNotFoundException, NoSuchCountryException, IOException {
        forwardTurnPhase();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gc.initAttack(gameId, attackingCountry, defendingCountry, units);
    }

    public void fight(Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws GameNotFoundException, NoSuchPlayerException, IOException, ClassNotFoundException, NoSuchCountryException, NotEnoughUnitsException, CountriesNotAdjacentException {
//        AttackResult ar = null;
//            ar = gc.fight(getGame().getId(), attCountry, defCountry, attackingUnits, defendingUnits);
            gc.fight(getGame().getId(), attackingCountry, defendingCountry, attackingUnits, defendingUnits);

//        componentMap.get(attackingCountry + "info-hbox").update();
//        componentMap.get(defendingCountry + "info-hbox").update();
//
//
//        getLog().update("Attacking Player: " + attCountry.getOwner() + " with country: " + attackingCountry + " rolled: ");
//        for (int attDices : ar.getAttackerDices()) {
//            getLog().update(attDices + "");
//        }
//        getLog().update("Defending Player: " + defCountry.getOwner() + " with country: " + defendingCountry + " rolled: ");
//        for (int defDices : ar.getDefenderDices()) {
//            getLog().update(defDices + "");
//        }
//
//        ((DiceGridPane) componentMap.get("dice-grid")).update(attCountry.getOwner().getColor().toString(), defCountry.getOwner().getColor().toString(), ar.getAttackerDices(), ar.getDefenderDices());
//
//        if (ar.getWinner() != null) {
//            if (ar.getWinner().equals(defCountry)) {
//                getLog().update(defendingCountry + " successfully defended. It is owned by: " + defCountry.getOwner());
//                setTurnManually(Phase.PERFORM_ANOTHER_ATTACK);
//            } else {
//                lastFightCountry = new LastFightCountries(attCountry, defCountry);
//                getLog().update(defendingCountry + " successfully attacked. It is now owned by: " + defCountry.getOwner());
//                forwardTurnPhase();
//            }
//        } else {
//            return;
//        }

    }

    public void trailUnits(int value) throws NoSuchPlayerException {
        try {
            gc.moveUnits(gameId, lastFightCountry.getSrcCountry(), lastFightCountry.getDestCountry(), value);
            componentMap.get(lastFightCountry.getSrcCountry() + "info-hbox").update();
            componentMap.get(lastFightCountry.getDestCountry() + "info-hbox").update();
            setTurnManually(Phase.ATTACK);
        } catch (IOException | CountriesNotAdjacentException | GameNotFoundException | NotEnoughUnitsException | NoSuchCountryException | CountryNotOwnedException e) {
            new ErrorAlert(e);
        }
    }

    public void move(String srcCountry, String destCountry, int amount) {
        Country srcCountryObj = getCountryFromString(srcCountry);
        Country destCountryObj = getCountryFromString(destCountry);
        try {
            gc.moveUnits(getGame().getId(), srcCountryObj, destCountryObj, amount);
            getLog().update(srcCountryObj.getOwner().getName() + " moved " + amount + " from " + srcCountry + " to " + destCountry);

        } catch (IOException | CountriesNotAdjacentException | GameNotFoundException | NotEnoughUnitsException | CountryNotOwnedException | NoSuchCountryException e) {
            new ErrorAlert(e);
        }
    }

    public void forwardTurnPhase() {
        try {
            gc.switchTurns(gameId);
//            componentMap.get("cards-hbox").update();
        } catch (GameNotFoundException | IOException | NoSuchCardException | NoSuchPlayerException | CardAlreadyOwnedException e) {
            new ErrorAlert(e);
        }
    }

    public void setTurnManually(Phase phase) throws NoSuchPlayerException, GameNotFoundException, IOException {
        gc.setTurn(gameId, phase);

//        getGame().getTurn().setPhase(phase);
//        Updatable dialogVBox = componentMap.get("dialog-vbox");
//        dialogVBox.update();
//        componentMap.get("player-list-vbox").update();
    }

    /**
     * method called when a country is clicked
     *
     * @param
     */
    public void countryClicked(String countryString) throws GameNotFoundException, IOException, ClassNotFoundException {
        if(myTurn()) {
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

    public Country getCountryFromString(String countryString) {
        try {
            return gc.getGameById(gameId).getCountries().get(countryString);
        } catch (IOException | ClassNotFoundException|  GameNotFoundException e) {
            new ErrorAlert(e);
            return null;
        }
    }

    // Mark: method to verify input locally, rather than requesting verification from the server
    //  since the server itself later verifies if the operation is legal,
    //  doing the input verification locally should be fine
    // FIXME: refactor to GameControllerFacade?
    // FIXME: Exceptions?
    public boolean hasHostileNeighbors(Country country){
        for (Country neighbor : country.getNeighbors()){
            if (!country.getOwner().equals(neighbor.getOwner()))
                return true;
        }
        return false;
    }

    public boolean myTurn() throws GameNotFoundException, IOException, ClassNotFoundException {
        // MARK:
        // FIXME: Game couldnt start at one point (nullPointerException), this fixes it
        //  i think it's because the game isn't initialized yet by the time we call this function
        if(gc.getGameById(gameId) != null) {
            return gc.getPlayerName().equals(getGame().getTurn().getPlayer().getName());
        } else {
            return false;
        }
    }

    public Player checkForWinner() {
        for (Player player : getGame().getPlayers()){
            try {
                if (gc.checkWinCondidtion(gameId, player)){
                    return player;
                }
            } catch (GameNotFoundException | NoSuchPlayerException | IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public Country getSelectedCountry() {
        return getGame().getCountries().get(selectedCountry);
    }

    public Player getCurrentPlayer() {
        return getGame().getTurn().getPlayer();
    }

    private LogHBox getLog() {
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
