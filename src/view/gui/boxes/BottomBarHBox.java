package view.gui.boxes;

import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class BottomBarHBox extends HBox implements RiskUIElement {


    public BottomBarHBox() {
        applyStyling(this, "bottom-bar-hbox", "bottom_bar_hbox.css");
        doStuff();
    }


    @Override
    public void doStuff() {
        this.setFillHeight(true);
        this.setPrefHeight(150);

        HBox logHBox = new LogHBox();
        HBox playerListHBox = new PlayerListHBox();
        HBox cardsHBox = new CardsHBox();
        HBox missionHBox = new MissionHBox();

        this.getChildren().addAll(logHBox, playerListHBox, cardsHBox, missionHBox);
    }
}
