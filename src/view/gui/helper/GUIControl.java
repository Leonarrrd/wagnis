package view.gui.helper;

import controller.GameController;
import datastructures.Color;
import exceptions.GameNotFoundException;
import exceptions.InvalidFormattedDataException;
import javafx.scene.control.Alert;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUIControl {

    private static GUIControl instance;
    public Map<String, Updatable> componentMap = new HashMap<>();
    private UUID gameId;
    private Map<String, CountryViewHelper> countryViewMap = MethodSlave.buildCountryViewMap();
    private GameController gc = GameController.getInstance();


    public Game getGame() {
//        if (gameId == null){
//           throw new NoIdHasBeenSetException ?
//        }
        try {
            return gc.getGameById(gameId);
        } catch (GameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GUIControl() {}

    public static GUIControl getInstance() {
        if(instance == null) {
            instance = new GUIControl();
        }
        return instance;
    }

    public void initNewGame(List<Player> players){
        Game game = null;
        try {
            game = GameController.getInstance().initNewGame();
        } catch (IOException | InvalidFormattedDataException e) {
            new ErrorAlert(Alert.AlertType.ERROR, e);
        }
        game.setPlayers(players);
        try {
            GameController.getInstance().setTurn(game.getId());
            GameController.getInstance().assignMissions(game.getId());
            GameController.getInstance().assignCountries(game.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.gameId = game.getId();
    }

    public void initLoadedGame(UUID gameId) {
        try {
            gc.loadGame(gameId);
        } catch (GameNotFoundException | IOException | InvalidFormattedDataException e){
            e.printStackTrace();
        }
        this.gameId = gameId;
        // FIXME: BULLSHIT FOR TESTING, since player colors arent implemented in savestates yet
        getGame().getPlayers().get(0).setColor(Color.RED);
        getGame().getPlayers().get(1).setColor(Color.BLUE);
        if (getGame().getPlayers().size() > 2) getGame().getPlayers().get(2).setColor(Color.GREEN);
        if (getGame().getPlayers().size() > 3) getGame().getPlayers().get(3).setColor(Color.YELLOW);
        if (getGame().getPlayers().size() > 4) getGame().getPlayers().get(4).setColor(Color.MACADAMIA);
    }


    public String trimColorCode(String rawColorCode){
        String colorCode = rawColorCode.substring(2,8);
        return colorCode.toUpperCase();
    }

    /**
     * Looks up a the corresponding Country to a color Code
     * IMPORTANT: the caller of this method needs to handle the case that the player did not click on a country (nothing should happen then)
     * @param colorCode
     * @return countryString if found, empty string if no country has been found for the param colorCode
     */
    public String getCountryStringFromColorCode(String colorCode){
        for (String countryString : countryViewMap.keySet()){
            if (countryViewMap.get(countryString).getColorCode().equals(colorCode)){
                return countryString;
            }
        }
        return "";
    }

    public void update() {

    }

    public Map<String, CountryViewHelper> getCountryViewMap(){
        return countryViewMap;
    }
}
