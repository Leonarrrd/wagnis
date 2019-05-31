package view.gui.viewhelper;

import view.gui.helper.Coordinate;

public class CountryViewHelper {
    private Coordinate coordinate;
    private String colorCode;

    public CountryViewHelper(Coordinate coordinate, String colorCode){
        this.coordinate = coordinate;
        this.colorCode = colorCode;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
