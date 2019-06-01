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
import view.gui.viewhelper.CountryViewHelper;

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
            GameController.getInstance().setTurn(game.getId());
            GameController.getInstance().assignMissions(game.getId());
            GameController.getInstance().assignCountries(game.getId());
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
        getGame().getPlayers().get(0).setColor(Color.RED);
        getGame().getPlayers().get(1).setColor(Color.BLUE);
        if (getGame().getPlayers().size() > 2) getGame().getPlayers().get(2).setColor(Color.GREEN);
        if (getGame().getPlayers().size() > 3) getGame().getPlayers().get(3).setColor(Color.YELLOW);
        if (getGame().getPlayers().size() > 4) getGame().getPlayers().get(4).setColor(Color.MACADAMIA);
    }

    public void placeUnits(int units) {
        try {
            GameController.getInstance().changeUnits(gameId, getGame().getCountries().get(selectedCountry), units);
            componentMap.get(selectedCountry + "info-hbox").update();
        } catch (GameNotFoundException | NoSuchCountryException e) {
            new ErrorAlert(e);
        }



        forwardTurnPhase();
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

        if(ar.getWinner() != null) {
            if(ar.getWinner().equals(defCountry)) {
                forwardTurnPhase();
            }
            forwardTurnPhase();
        } else {
            return;
        }

    }

    public void forwardTurnPhase() {
        try {

            GameController.getInstance().switchTurns(gameId);
            Updatable dialogVBox = componentMap.get("dialog-vbox");
            dialogVBox.update();
            componentMap.get("player-list-vbox").update();

        } catch (GameNotFoundException e) {
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
     * @param colorCode
     */
    public void countryClicked(String colorCode) {
        selectedCountry = getCountryStringFromColorCode(colorCode);
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
     *
     * @param colorCode
     * @return countryString if found, empty string if no country has been found for the param colorCode
     */
    private String getCountryStringFromColorCode(String colorCode) {
        for (String countryString : countryViewMap.keySet()) {
            if (countryViewMap.get(countryString).getColorCode().equals(colorCode)) {
                return countryString;
            }
        }
        return "";
    }


    public Map<String, CountryViewHelper> getCountryViewMap() {
        return countryViewMap;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

}
