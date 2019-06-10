package view.gui.helper;

import controller.GameController;
import controller.GraphController;
import datastructures.Color;
import datastructures.Phase;
import exceptions.*;
import model.AttackResult;
import model.Country;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.LogHBox;
import view.gui.panes.DiceGridPane;
import view.gui.viewhelper.CountryViewHelper;
import view.gui.viewhelper.LastFightCountries;

import java.io.IOException;
import java.util.*;


public class GUIControl {

    private static GUIControl instance;
    public Map<String, Updatable> componentMap = new HashMap<>();
    private UUID gameId;
    private Map<String, CountryViewHelper> countryViewMap = CountryRelationLoadHelper.buildCountryViewMap();
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
        getGame().getPlayers().get(0).setColor(Color.BLUE);
        getGame().getPlayers().get(1).setColor(Color.RED);
        if (getGame().getPlayers().size() > 2) getGame().getPlayers().get(2).setColor(Color.GREEN);
        if (getGame().getPlayers().size() > 3) getGame().getPlayers().get(3).setColor(Color.YELLOW);
        if (getGame().getPlayers().size() > 4) getGame().getPlayers().get(4).setColor(Color.MACADAMIA);
        // MARK: FOR TESTING
        getGame().getTurn().getPlayer().setUnitsToPlace(7);
    }

    // TODO: still oversimplified
    public void useCards(int infantryCards, int cavalryCards, int artilleryCards){
        try {
            GameController.getInstance().useCards(gameId, getCurrentPlayer(), infantryCards, cavalryCards, artilleryCards);
        } catch (GameNotFoundException | NoSuchCardException | NoSuchPlayerException e) {
            e.printStackTrace();
        }
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


        getLog().update("Attacking Player: " + attCountry.getOwner() + " with country: " + attackingCountry + " rolled: ");
        for (int attDices : ar.getAttackerDices()) {
            getLog().update(attDices + "");
        }
        getLog().update("Defending Player: " + defCountry.getOwner() + " with country: " + defendingCountry + " rolled: ");
        for (int defDices : ar.getDefenderDices()) {
            getLog().update(defDices + "");
        }

        ((DiceGridPane) componentMap.get("dice-grid")).update(attCountry.getOwner().getColor().toString(), defCountry.getOwner().getColor().toString(), ar.getAttackerDices(), ar.getDefenderDices());

        if (ar.getWinner() != null) {
            if (ar.getWinner().equals(defCountry)) {
                getLog().update(defendingCountry + " successfully defended. It is owned by: " + defCountry.getOwner());
                setTurnManually(Phase.PERFORM_ANOTHER_ATTACK);
            } else {
                lastFightCountry = new LastFightCountries(attCountry, defCountry);
                getLog().update(defendingCountry + " successfully attacked. It is now owned by: " + defCountry.getOwner());
                forwardTurnPhase();
            }
        } else {
            return;
        }

    }

    public void trailUnits(int value) {
        try {
            gc.moveUnits(gameId, lastFightCountry.getSrcCountry(), lastFightCountry.getDestCountry(), value);
            componentMap.get(lastFightCountry.getSrcCountry() + "info-hbox").update();
            componentMap.get(lastFightCountry.getDestCountry() + "info-hbox").update();
            setTurnManually(Phase.ATTACK);
        } catch (CountriesNotAdjacentException | GameNotFoundException | NotEnoughUnitsException | NoSuchCountryException | CountryNotOwnedException e) {
            new ErrorAlert(e);
        }
    }

    public void move(String srcCountry, String destCountry, int amount) {
        Country srcCountryObj = getCountryFromString(srcCountry);
        Country destCountryObj = getCountryFromString(destCountry);
        try {
            gc.moveUnits(getGame().getId(), srcCountryObj, destCountryObj, amount);
            getLog().update(srcCountryObj.getOwner().getName() + " moved " + amount + " from " + srcCountry + " to " + destCountry);

        } catch (CountriesNotAdjacentException | GameNotFoundException | NotEnoughUnitsException | CountryNotOwnedException | NoSuchCountryException e) {
            new ErrorAlert(e);
        }
        componentMap.get(srcCountry + "info-hbox").update();
        componentMap.get(destCountry + "info-hbox").update();

    }

    public void forwardTurnPhase() {
        try {
            gc.switchTurns(gameId);
            componentMap.get("dialog-vbox").update();
            componentMap.get("player-list-vbox").update();
            componentMap.get("mission-hbox").update();
            componentMap.get("cards-hbox").update();
        } catch (GameNotFoundException | IOException | NoSuchCardException | NoSuchPlayerException | CardAlreadyOwnedException e) {
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
        } catch (GameNotFoundException e) {
            e.printStackTrace();
            return null;
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

    public boolean hasCountryToMoveTo(Country country){
            List<String> countriesInGraph = GraphController.getInstance().getPlayerGraphMap().get(country.getOwner()).evaluateCountriesAllowedToMoveTo(country.getName());
            return countriesInGraph.size() == 1;
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
}
