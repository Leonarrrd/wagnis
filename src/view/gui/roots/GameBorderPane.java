package view.gui.roots;

import controller.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.gui.Main;
import view.gui.boxes.BottomBarHBox;
import view.gui.boxes.CountryInfoHBox;
import view.gui.boxes.DialogVBox;
import view.gui.helper.GUIControl;
import view.gui.helper.MethodSlave;
import view.gui.helper.RiskUIElement;
import view.gui.boxes.BottomBarNodeHBox;
import view.gui.util.Util;

import java.io.File;
import java.sql.SQLOutput;

import static view.gui.util.UIConstants.IMG_RESOURCE_PATH;
import static view.gui.util.UIConstants.WINDOW_WIDTH;


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
                GUIControl.getInstance().countryClicked(colorCode);
                System.out.println();
            }
        });

        VBox dialogVBox = new DialogVBox();
        dialogVBox.setLayoutY(670);


        Pane pane = new Pane();
        pane.getChildren().add(mapImageView);
        pane.getChildren().add(dialogVBox);
        pane.getChildren().addAll(MethodSlave.buildCountryInfoBoxes());


        BottomBarHBox bottomBar = new BottomBarHBox();

        this.setBottom(bottomBar);
        this.setTop(pane);

    }
}
