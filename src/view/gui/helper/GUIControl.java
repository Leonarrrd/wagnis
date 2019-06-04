package view.gui.helper;

import controller.GameController;
import datastructures.Color;
import datastructures.Phase;
import exceptions.*;
import javafx.scene.control.Alert;
import model.AttackResult;
import model.Country;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.LogHBox;
import view.gui.viewhelper.CountryViewHelper;
import view.gui.viewhelper.LastFightCountries;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static javafx.scene.control.Alert.AlertType.ERROR;


public class GUIControl {

    private static GUIControl instance;
    public Map<String, Updatable> componentMap = new HashMap<>();
    private UUID gameId;
    private Map<String, CountryViewHelper> countryViewMap = MethodSlave.buildCountryViewMap();
    private GameController gc = GameController.getInstance();
    private String selectedCountry;
    private LastFightCountries lastFightCountry;



    public Game getGame() {

        try {
            return gc.getGameById(gameId);
        } catch (GameNotFoundException e) {
            new ErrorAlert(e);
            return null;
        }
    }

    private GUIControl() {
    }

    public static GUIControl getInstance() {
        if (instance == null) {
            instance = new GUIControl();
        }
        return instance;
    }

    public void initNewGame(List<Player> players) {
        Game game = null;
        try {
            game = GameController.getInstance().initNewGame();
        } catch (IOException | InvalidFormattedDataException e) {
            new ErrorAlert(e);
        }
        game.setPlayers(players);
        try {
            GameController.getInstance().assignMissions(game.getId());
            GameController.getInstance().assignCountries(game.getId());
            GameController.getInstance().setTurn(game.getId());
        } catch (Exception e) {
            new ErrorAlert(e);
        }
        this.gameId = game.getId();
    }


    public void initLoadedGame(UUID gameId) {
        try {
            gc.loadGame(gameId);
        } catch (GameNotFoundException | IOException | InvalidFormattedDataException e) {
            new ErrorAlert(e);
        }
        this.gameId = gameId;
        // FIXME: BULLSHIT FOR TESTING, since player colors arent implemented in savestates yet
        getGame().getTurn().getPlayer().setUnitsToPlace(7);
        getGame().getPlayers().get(0).setColor(Color.RED);
        getGame().getPlayers().get(1).setColor(Color.BLUE);
        if (getGame().getPlayers().size() > 2) getGame().getPlayers().get(2).setColor(Color.GREEN);
        if (getGame().getPlayers().size() > 3) getGame().getPlayers().get(3).setColor(Color.YELLOW);
        if (getGame().getPlayers().size() > 4) getGame().getPlayers().get(4).setColor(Color.MACADAMIA);
    }

    public void placeUnits(int units) {
        try {
            Country c = getGame().getCountries().get(selectedCountry);
            GameController.getInstance().changeUnits(gameId, c, units);
            GameController.getInstance().changeUnitsToPlace(gameId, getGame().getTurn().getPlayer(), -units);
            componentMap.get(selectedCountry + "info-hbox").update();
            getLog().update(c.getOwner() + " placed " + units + " units on " + c.getName());
        } catch (GameNotFoundException | NoSuchCountryException | NoSuchPlayerException e) {
            new ErrorAlert(e);
        }
        //FIXME: Erst prüfen ob noch weitere Einheiten platziert werden können
        // Erst wenn keine Einheiten mehr platziert werden können springen wir in die nächste Phase
        if (getGame().getTurn().getPlayer().getUnitsToPlace() == 0) {
            forwardTurnPhase();
        } else {
            Updatable dialogVBox = componentMap.get("dialog-vbox");
            dialogVBox.update();
        }
    }

    public void fight(String attackingCountry, String defendingCountry, int attackingUnits, int defendingUnits) {
        Country attCountry = getGame().getCountries().get(attackingCountry);
        Country defCountry = getGame().getCountries().get(defendingCountry);
        AttackResult ar = null;
        try {
            ar = GameController.getInstance().fight(getGame().getId(), attCountry, defCountry, attackingUnits, defendingUnits);
        } catch (NotEnoughUnitsException | CountriesNotAdjacentException | GameNotFoundException | NoSuchCountryException e) {
            new ErrorAlert(e);
        }
        componentMap.get(attackingCountry + "info-hbox").update();
        componentMap.get(defendingCountry + "info-hbox").update();


        getLog().update("Attacking Player: " + attCountry.getOwner() +" with country: " + attackingCountry + " rolled: ");
        for(int attDices : ar.getAttackerDices()) {
            getLog().update(attDices + "");
        }
        getLog().update("Defending Player: " + defCountry.getOwner() +" with country: " + defendingCountry + " rolled: ");
        for(int defDices : ar.getDefenderDices()) {
            getLog().update(defDices + "");
        }


        if(ar.getWinner() != null) {
            if(ar.getWinner().equals(defCountry)) {
                getLog().update(defendingCountry + " successfully defended. It is owned by: " + defCountry.getOwner());
                forwardTurnPhase();
            } else {
                lastFightCountry = new LastFightCountries(attCountry, defCountry);
                getLog().update(defendingCountry + " successfully attacked. It is now owned by: " + defCountry.getOwner());
            }

            forwardTurnPhase();
        } else {
            return;
        }

    }

    public void trailUnits(int value) {
        try {
            GameController.getInstance().moveUnits(gameId, lastFightCountry.getSrcCountry(), lastFightCountry.getDestCountry(), value);
            componentMap.get(lastFightCountry.getSrcCountry() + "info-hbox").update();
            componentMap.get(lastFightCountry.getDestCountry() + "info-hbox").update();
            setTurnManually(Phase.ATTACK);
        } catch (GameNotFoundException | NotEnoughUnitsException | NoSuchCountryException | CountryNotOwnedException | NoSuchPlayerException e) {
            new ErrorAlert(e);
        }
    }

    public void move(String srcCountry, String destCountry, int amount) {
        Country srcCountryObj = getCountryFromString(srcCountry);
        Country destCountryObj = getCountryFromString(destCountry);
        try {
            GameController.getInstance().moveUnits(getGame().getId(), srcCountryObj, destCountryObj, amount);
            getLog().update(srcCountryObj.getOwner().getName() + " moved " + amount + " from " + srcCountry + " to " + destCountry);

        } catch (GameNotFoundException | NotEnoughUnitsException | CountryNotOwnedException | NoSuchCountryException | NoSuchPlayerException e) {
            new ErrorAlert(e);
        }
        componentMap.get(srcCountry+ "info-hbox").update();
        componentMap.get(destCountry + "info-hbox").update();

    }

    public void forwardTurnPhase() {
        try {
            GameController.getInstance().switchTurns(gameId);
            componentMap.get("dialog-vbox").update();
            componentMap.get("player-list-vbox").update();

        } catch (GameNotFoundException | NoSuchCardException | NoSuchPlayerException | CardAlreadyOwnedException e) {
            new ErrorAlert(e);
        }
    }

    public void setTurnManually(Phase phase) {
        getGame().getTurn().setPhase(phase);
        Updatable dialogVBox = componentMap.get("dialog-vbox");
        dialogVBox.update();
        componentMap.get("player-list-vbox").update();
    }

    /**
     * method called when a country is clicked
     *
     * @param
     */
    public void countryClicked(String countryString) {
        // MARK: right now, selectedCountry will only be updated if a country is clicked
            selectedCountry = countryString;
            switch (getGame().getTurn().getPhase()) {
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

    /**
     * Looks up a the corresponding Country to a color Code
     * IMPORTANT: the caller of this method needs to handle the case that the player did not click on a country (nothing should happen then)
     * MARK: Maybe throw an exception instead of returning null
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
            return GameController.getInstance().getGameById(gameId).getCountries().get(countryString);
        } catch (GameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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


}
