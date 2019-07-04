package view.gui.roots;

import exceptions.GameNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.BottomBarHBox;
import view.gui.boxes.DialogVBox;
import view.gui.buttons.SaveButton;
import view.gui.helper.GUIControl;
import view.gui.helper.CountryRelationLoadHelper;
import view.gui.helper.RiskUIElement;
import view.gui.panes.DiceGridPane;
import view.gui.util.Util;

import java.io.File;
import java.io.IOException;


public class GameBorderPane extends BorderPane implements RiskUIElement {

    public GameBorderPane() {
        applyStyling(this, "game-vbox", "game_vbox.css");
        init();
    }

    @Override
    public void init() {

        ImageView mapImageView = new ImageView(new Image("file:assets" + File.separator + "img" + File.separator + "map.png"));
        mapImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String rawColorCode = mapImageView.getImage().getPixelReader().getColor((int) event.getX(), (int) event.getY()).toString();
                String colorCode = Util.trimColorCode(rawColorCode);
                if (GUIControl.getInstance().getCountryStringFromColorCode(colorCode) != null) {
                    try {
                        GUIControl.getInstance().countryClicked(GUIControl.getInstance().getCountryStringFromColorCode(colorCode));
                    } catch (GameNotFoundException | IOException | ClassNotFoundException e) {
                        new ErrorAlert(e);
                    }
                }
            }
        });

        VBox dialogVBox = new DialogVBox();
        GridPane dices = new DiceGridPane();
        SaveButton saveButton = new SaveButton();

        Pane pane = new Pane();
        pane.getChildren().add(mapImageView);
        pane.getChildren().add(dialogVBox);
        pane.getChildren().addAll(CountryRelationLoadHelper.buildCountryInfoBoxes());
        pane.getChildren().add(dices);
        pane.getChildren().add(saveButton);


        BottomBarHBox bottomBar = new BottomBarHBox();

        this.setBottom(bottomBar);
        this.setTop(pane);
    }
}
