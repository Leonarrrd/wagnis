package view.gui.viewhelper;

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
