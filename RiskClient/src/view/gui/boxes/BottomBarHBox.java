package view.gui.boxes;

import javafx.scene.layout.*;
import view.gui.helper.RiskUIElement;

public class BottomBarHBox extends HBox implements RiskUIElement {


    public BottomBarHBox() {
        applyStyling(this, "bottom-bar-hbox", "bottom_bar_hbox.css");
        init();
    }


    @Override
    public void init() {
        this.setFillHeight(true);
        this.setPrefHeight(150);

        HBox logHBox = new LogHBox();
        HBox playerListHBox = new PlayerListVBox();
        HBox cardsHBox = new CardsHBox();
        HBox missionHBox = new MissionHBox();

        this.getChildren().addAll(logHBox, playerListHBox, cardsHBox, missionHBox);
    }
}
