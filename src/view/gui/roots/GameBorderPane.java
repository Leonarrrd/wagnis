package view.gui.roots;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class GameBorderPane extends VBox implements RiskUIElement {

    public GameBorderPane() {
        applyStyling(this, "game-vbox", "game_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {

        VBox vbox = new VBox();


        vbox.setAlignment(Pos.BOTTOM_LEFT);
        HBox hbox = new HBox();
        hbox.setStyle("");
        hbox.getChildren().add(new ImageView("https://pbs.twimg.com/profile_images/1044786302443372544/3bIbMHgS_400x400.jpg"));

        vbox.getChildren().add(hbox);



        HBox hboxBottom = new HBox();
        hboxBottom.getChildren().add(new Text("Log"));
        hboxBottom.getChildren().add(new Text("Player"));
        hboxBottom.getChildren().add(new Text("Karten"));
        hboxBottom.getChildren().add(new Text("Mission"));


    }
}
