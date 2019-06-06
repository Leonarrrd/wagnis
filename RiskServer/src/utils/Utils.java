package utils;

public class Utils {

    public static boolean stringContainsDelimitters(String string) {
        if (string.contains("[")
                || string.contains("]")
                || string.contains(":")
                || string.contains("'")
                || string.contains(",")
                || string.contains("{")
                || string.contains("}")
                || string.contains("-")
                || string.contains(";")
        ) {
            return true;
        }
        return false;
    }
}

