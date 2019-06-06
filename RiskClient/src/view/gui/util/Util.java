package view.gui.util;

public class Util {

    public static String trimColorCode(String rawColorCode){
        String colorCode = rawColorCode.substring(2,8);
        return colorCode.toUpperCase();
    }

}
