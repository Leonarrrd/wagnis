package view.gui.roots;

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
import view.gui.boxes.BottomBarHBox;
import view.gui.boxes.DialogVBox;
import view.gui.helper.RiskUIElement;
import view.gui.boxes.BottomBarNodeHBox;

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
                System.out.println(mapImageView.getImage().getPixelReader().getColor((int)event.getX(),(int) event.getY()).toString());
            }
        });

        VBox dialogVBox = new DialogVBox();

        StackPane sp = new StackPane();

        sp.getChildren().add(mapImageView);
        sp.getChildren().add(dialogVBox);
        sp.setAlignment(Pos.BOTTOM_LEFT);
        BottomBarHBox bottomBar = new BottomBarHBox();

        this.setBottom(bottomBar);
        this.setTop(sp);

    }
}
