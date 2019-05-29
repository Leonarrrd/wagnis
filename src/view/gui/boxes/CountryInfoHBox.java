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
    private ImageView flag;
    private Text units;

    public CountryInfoHBox(String countryString) {
        this.countryString = countryString;
        //FIXME: so wie du dir das vorgestellt hast mit Updatable?
//        addAsUpdateElement(countryString + "InfoBox, this);
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
        units = new Text("1");
        units.setStyle("-fx-font: 20 verdana;");

        HBox unitsWrapper = new HBox();
        unitsWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
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
     * FIXME: Not hooked up to the game yet
     * FIXME: Probably better to have the images loaded somewhere and just set them here instead of buffering them again and again
     * @return an image URL to a flag image whose color matches the country owner
     */
    public String getFlagImageUrl(){
//        String url = "";
//        switch (country.getOwner().getColor()){
//            case RED:
//                url = "file:assets/img/flagRed.png";
//                break;
//            case BLUE:
//                url = "file:assets/img/flagBlue.png";
//                break;
//            //etc.
//        }
//        return url;
        return "file:assets/img/flagRed.png";
    }

    /**
     * tested with dummy objects --> worked
     */
    public void update(){
//        this.flag.setImage(new Image(getFlagImageUrl()));
//        this.units.setText( "" + country.getUnits());
    }
}
