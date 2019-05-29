package view.gui.helper;

import controller.GameController;

import java.util.HashMap;
import java.util.Map;

public class GUIControl {

    private static GUIControl instance;
    public Map<String, Updatable> componentMap = new HashMap<>();

    private GUIControl() {}

    public static GUIControl getInstance() {
        if(instance == null) {
            instance = new GUIControl();
        }
        return instance;
    }

    private GameController gc = GameController.getInstance();
    private Map<String, CountryViewHelper> countryViewMap = MethodSlave.buildCountryViewMap();

    public Map<String, CountryViewHelper> getCountryViewMap(){
        return countryViewMap;
    }

    public String trimColorCode(String rawColorCode){
        String colorCode = rawColorCode.substring(2,8);
        return colorCode.toUpperCase();
    }

    /**
     * Looks up a the corresponding Country to a color Code
     * IMPORTANT: the caller of this method needs to handle the case that the player did not click on a country (nothing should happen then)
     * @param colorCode
     * @return countryString if found, empty string if no country has been found for the colorCode
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
}
