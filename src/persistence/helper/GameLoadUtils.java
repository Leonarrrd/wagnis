package persistence.helper;

import persistence.helpermodels.RawCountryData;

import java.util.HashMap;
import java.util.Map;

public class GameLoadUtils {

    public static String[] evaluatePlayerArrayFromDatString(String dataString) {
        dataString = dataString.replace("[", "").replace("]", "");
        String[] split = dataString.split(":");
        return split;
    }

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

    public static void main(String... args) {
        Map<Integer, RawCountryData[]> x = evaluateCountryData("{[1'2-5'6]:[3'2-2'7]:[4'6-6'2]}");
    }

}

