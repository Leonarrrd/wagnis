package view.gui.helper;

public class CountryViewHelper {
    private Coordinate coordinate;
    private String colorCode;

    public CountryViewHelper(Coordinate coordinate, String colorCode){
//        this.countryString = countryString;
        this.coordinate = coordinate;
        this.colorCode = colorCode;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getColorCode() {
        return colorCode;
    }
}
