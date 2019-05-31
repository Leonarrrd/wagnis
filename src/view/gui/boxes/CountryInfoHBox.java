package view.gui.boxes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

/**
 * FIXME: Currently not hooked up to any game
 * FIXME: Needs styling
 * one of these exists for each country on the map
 * HBox that consists of a flag and a box with a number
 * the flag marks the current owner of the country
 * the number stands for the amount of units currently present on the country
 */
public class CountryInfoHBox extends HBox implements RiskUIElement, Updatable {
    private String countryString;
    // following two might be unncecessary because the references to these objects are already stored in children array,
    // but I dont know how to invoke methods like .setImage(...) from objects accessed via node.getChildren.get(index)
    // update: it's actually possible using typecasting: something like
    // ImageView iv = (ImageView) this.getChildren.get(0);
    // ImageView.setImage(...)
    // but I think the way it currently is is better
    private ImageView flag;
    private Text units;

    public CountryInfoHBox(String countryString) {
        this.countryString = countryString;
        applyStyling(this, "country-info-hbox", "country_info_hbox.css");
        addAsUpdateElement(countryString + "InfoHBox", this);
        doStuff();
    }


    //a lot of code below will be refactored to be loaded from css
    public void doStuff(){
        this.setLayoutX(GUIControl.getInstance().getCountryViewMap().get(countryString).getCoordinate().getX());
        // -50 because I'm very smart
        this.setLayoutY(GUIControl.getInstance().getCountryViewMap().get(countryString).getCoordinate().getY() - 50);
        this.setAlignment(Pos.BOTTOM_RIGHT);

        flag = new ImageView(new Image(getFlagImageUrl()));

//        units = new Text("" + country.getUnits());
        units = new Text("" + GUIControl.getInstance().getGame().getCountries().get(countryString).getUnits());
        units.setStyle("-fx-font: 20 verdana;");

        HBox unitsWrapper = new HBox();
        unitsWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
        unitsWrapper.setAlignment(Pos.BOTTOM_LEFT);
        unitsWrapper.setMaxHeight(5);
        unitsWrapper.getChildren().add(units);

        this.getChildren().add(flag);
        this.getChildren().add(unitsWrapper);

        // FIXME: Not sure if we want this
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(countryString);
            }
        });
    }

    /**
     * Maybe bad practice to build the URL from the enum.name()
     * FIXME: Probably better to have the images loaded somewhere and just set them here instead of loading them from file again and again
     * @return an image URL to a flag image whose color matches the country owner
     */
    public String getFlagImageUrl(){
//        String url = "";
//        switch (GUIControl.getInstance().getGame().getCountries().get(countryString).getOwner().getColor()){
//            case RED:
//                url = "file:assets/img/flagRed.png";
//                break;
//            case BLUE:
//                url = "file:assets/img/flagBlue.png";
//                break;
//            //etc.
//        }
//        return url;
        return  "file:assets/img/flag"
                + GUIControl.getInstance().getGame().getCountries().get(countryString).getOwner().getColor().name()
                + ".png";
//        return "file:assets/img/flagRed.png"; // FOR TESTING IF DUMMY IS NEEDED
    }


    /**
     * tested with dummy objects --> updating worked
     */
    public void update(){
        this.flag.setImage(new Image(getFlagImageUrl()));
        // maybe also put this in own function?
        this.units.setText( "" + GUIControl.getInstance().getGame().getCountries().get(countryString).getUnits());
    }
}
