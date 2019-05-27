package view.gui.roots;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
//        image.fitHeightProperty().bind(this.heightProperty()); // something like that

        BorderPane bp = new BorderPane();
        Button button = new Button("Im a button");
        VBox dialogVBox = new DialogVBox();

        bp.setBottom(dialogVBox);

        StackPane sp = new StackPane();
        sp.getChildren().add(mapImageView);
        sp.getChildren().add(bp);

        BottomBarHBox bottomBar = new BottomBarHBox();

        this.setBottom(bottomBar);
        this.setTop(sp);

    }
}
