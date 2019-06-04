package view.gui.roots;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.gui.boxes.BottomBarHBox;
import view.gui.boxes.DialogVBox;
import view.gui.helper.GUIControl;
import view.gui.helper.CountryRelationLoadHelper;
import view.gui.helper.RiskUIElement;
import view.gui.util.Util;


public class GameBorderPane extends BorderPane implements RiskUIElement {

    public GameBorderPane() {
        applyStyling(this, "game-vbox", "game_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {

        ImageView mapImageView = new ImageView(new Image("file:assets/img/map.png"));
        mapImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String rawColorCode = mapImageView.getImage().getPixelReader().getColor((int)event.getX(),(int) event.getY()).toString();
                String colorCode = Util.trimColorCode(rawColorCode);
                if (GUIControl.getInstance().getCountryStringFromColorCode(colorCode) != null) {
                    GUIControl.getInstance().countryClicked(GUIControl.getInstance().getCountryStringFromColorCode(colorCode));
                }
            }
        });

        VBox dialogVBox = new DialogVBox();

        dialogVBox.setLayoutY(640);
        dialogVBox.setLayoutX(10);


        Pane pane = new Pane();
        pane.getChildren().add(mapImageView);
        pane.getChildren().add(dialogVBox);
        pane.getChildren().addAll(CountryRelationLoadHelper.buildCountryInfoBoxes());


        BottomBarHBox bottomBar = new BottomBarHBox();

        this.setBottom(bottomBar);
        this.setTop(pane);
    }
}
