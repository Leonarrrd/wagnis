package view.gui.helper;

import view.gui.boxes.CountryInfoHBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for methods that i didnt know where to put
 * Maybe make this some kind of init class?
 */
public class MethodSlave {
    /**
     * Highly complicated algorithm, don't worry if you dont't understand it
     * @return
     */
    public static  Map<String, CountryViewHelper> buildCountryViewMap() {
        Map<String, CountryViewHelper> countryViewMap = new HashMap<>();
        countryViewMap.put("Alaska", new CountryViewHelper(new Coordinate(77, 113), "E3DF26"));
        countryViewMap.put("Alberta", new CountryViewHelper(new Coordinate(145, 255), "FFC72A"));
        countryViewMap.put("Central America", new CountryViewHelper(new Coordinate(165, 455), "F6CF3F"));
        countryViewMap.put("Eastern United States", new CountryViewHelper(new Coordinate(260, 355), "FFDA27"));
        countryViewMap.put("Greenland", new CountryViewHelper(new Coordinate(400, 110), "D9CA1D"));
        countryViewMap.put("Northwest Territory", new CountryViewHelper(new Coordinate(140, 150), "FFEB14"));
        countryViewMap.put("Ontario", new CountryViewHelper(new Coordinate(230, 250), "F1D384"));
        countryViewMap.put("Quebec", new CountryViewHelper(new Coordinate(310, 230), "ECE819"));
        countryViewMap.put("Western United States", new CountryViewHelper(new Coordinate(145, 325), "FED857"));
        countryViewMap.put("Argentina", new CountryViewHelper(new Coordinate(405, 710), "9E0B0F"));
        countryViewMap.put("Brazil", new CountryViewHelper(new Coordinate(465, 625), "A0410D"));
        countryViewMap.put("Venezuela", new CountryViewHelper(new Coordinate(355, 555), "A36209"));
        countryViewMap.put("Great Britain", new CountryViewHelper(new Coordinate(670 , 212), "004A80"));
        countryViewMap.put("Iceland", new CountryViewHelper(new Coordinate(600, 165), "0000FF"));
        countryViewMap.put("Northern Europe", new CountryViewHelper(new Coordinate(745, 260), "7777D5"));
        countryViewMap.put("Scandinavia", new CountryViewHelper(new Coordinate(755, 145), "0076A3"));
        countryViewMap.put("Southern Europe", new CountryViewHelper(new Coordinate(750, 305), "7DA7D9"));
        countryViewMap.put("Ukraine", new CountryViewHelper(new Coordinate(825, 230), "00AEEF"));
        countryViewMap.put("Western Europe", new CountryViewHelper(new Coordinate(665, 305), "6DCFF6"));
        countryViewMap.put("Congo", new CountryViewHelper(new Coordinate(755, 555), "F49AC1"));
        countryViewMap.put("East Africa", new CountryViewHelper(new Coordinate(845, 570), "F26D7D"));
        countryViewMap.put("Egypt", new CountryViewHelper(new Coordinate(800, 450), "F5989D"));
        countryViewMap.put("Madagascar", new CountryViewHelper(new Coordinate(907, 630), "ED145B"));
        countryViewMap.put("North Africa", new CountryViewHelper(new Coordinate(670, 450), "F26C4F"));
        countryViewMap.put("South Africa", new CountryViewHelper(new Coordinate(800, 650), "F06EAA"));
        countryViewMap.put("Afghanistan", new CountryViewHelper(new Coordinate(945, 280), "005E20"));
        countryViewMap.put("India", new CountryViewHelper(new Coordinate(1020, 405), "005952"));
        countryViewMap.put("Irkutsk", new CountryViewHelper(new Coordinate(1150, 190), "208523"));
        countryViewMap.put("Japan", new CountryViewHelper(new Coordinate(1260, 315), "00A651"));
        countryViewMap.put("Kamchatka", new CountryViewHelper(new Coordinate(1280, 80), "62EC67"));
        countryViewMap.put("Middle East", new CountryViewHelper(new Coordinate(870, 400), "005826"));
        countryViewMap.put("Mongolia", new CountryViewHelper(new Coordinate(1165, 270), "59D35D"));
        countryViewMap.put("Siam", new CountryViewHelper(new Coordinate(1115, 405), "8DC63F"));
        countryViewMap.put("Siberia", new CountryViewHelper(new Coordinate(1050, 150), "19A51E"));
        countryViewMap.put("Ural", new CountryViewHelper(new Coordinate(970, 170), "406618"));
        countryViewMap.put("Yakutsk", new CountryViewHelper(new Coordinate(1155, 85), "26CC2B"));
        countryViewMap.put("Eastern Australia", new CountryViewHelper(new Coordinate(1315, 645), "4B0049"));
        countryViewMap.put("Western Australia", new CountryViewHelper(new Coordinate(1230, 635), "32004B"));
        countryViewMap.put("Indonesia", new CountryViewHelper(new Coordinate(1183, 508), "662D91"));
        countryViewMap.put("New Guinea", new CountryViewHelper(new Coordinate(1315, 535), "92278F"));
        countryViewMap.put("China", new CountryViewHelper(new Coordinate(1100, 340), "598527"));
        return countryViewMap;
    }

    public static List<CountryInfoHBox> buildCountryInfoBoxes(){
        List<CountryInfoHBox> nodes = new ArrayList<>();
        for (String countryString : GUIControl.getInstance().getCountryViewMap().keySet()){
            nodes.add(new CountryInfoHBox(countryString));
        }
        return nodes;
    }
}
