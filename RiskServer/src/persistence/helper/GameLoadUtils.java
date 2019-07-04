package persistence.helper;

import persistence.helpermodels.RawCountryData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLoadUtils {

    public static final String PROJECT_DATA_DIR = System.getProperty("user.dir") + "/../RiskServer/data/";

    /**
     * parses player data
     * @param dataString
     * @return
     */
    public static String[] evaluatePlayerArrayFromDataString(String dataString) {
        dataString = dataString.replace("[", "").replace("]", "");
        String[] split = dataString.split(":");
        return split;
    }

    /**
     * parses country data
     * @param dataString
     * @return
     */
    public static  Map<Integer, RawCountryData[]> evaluateCountryData(String dataString) {
        dataString = dataString.replace("{", "").replace( "}", ""); //[1(2)*5(6)]:[3(2)*2(7)]:[4(6)*6(2)]},[1:2:3],{[1*2]:[4*5]:[7*8]
        String[] split = dataString.split(":"); // [1(2)*5(6)]

        Map<Integer, RawCountryData[]> playerIndicesCountryDataMap = new HashMap<>();

        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].replace("[", "").replace("]", ""); //1(2)*5(6)

            String[] subSplit = split[i].split("-"); //1'2
            RawCountryData[] rawCountryDatas = new RawCountryData[subSplit.length];
            for (int j = 0; j < subSplit.length; j++) {
                String[] subSubSplit = subSplit[j].split("'");// subSubSplit[0] = countryindeces, subSubSplit[1] = amountUnits
                int countryIndex = Integer.parseInt(subSubSplit[0]);
                int amountUnits = Integer.parseInt(subSubSplit[1]);
                RawCountryData r = new RawCountryData(countryIndex, amountUnits);
                rawCountryDatas[j] = r;

            }
            playerIndicesCountryDataMap.put(i, rawCountryDatas);
        }
        return playerIndicesCountryDataMap;
    }

    /**
     * parses the mission data
     * @param dataString
     * @return
     */
    public static List<Integer> evaluateMissionData(String dataString){
        List<Integer> missionIds = new ArrayList<>();
        dataString = dataString.replace("[", "").replace("]", "");
        String [] split = dataString.split(":");
        for (int i = 0; i < split.length; i++){
            int missionId = Integer.parseInt(split[i]);
            missionIds.add(missionId);
        }
        return missionIds;
    }

    /**
     * parses the card Data
     * @param dataString
     * @return
     */
    public static Map<Integer, List<Integer>> evaluateCardData(String dataString){
        Map<Integer, List<Integer>> cardData = new HashMap<>();
        dataString = dataString.replace("[", "").replace("]", "");
        dataString = dataString.replace("{", "").replace("}", "");
        String[] split = dataString.split(":");
        for (int i = 0; i < split.length; i++){
            cardData.put(i, new ArrayList<>());
            String s = split[i];
            String[] cardIds = s.split("-");
            for (String field : cardIds){
                if(!field.equals("")) {
                cardData.get(i).add(Integer.parseInt(field));
                }
            }
        }
        return cardData;
    }
}

