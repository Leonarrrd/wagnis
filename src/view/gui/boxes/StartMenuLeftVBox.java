package view.gui.boxes;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.gui.eventhandler.ExitEventHandler;
import view.gui.eventhandler.LoadGameEventHandler;
import view.gui.eventhandler.NewGameEventHandler;
import view.gui.helper.RiskUIElement;

public class StartMenuLeftVBox extends VBox implements RiskUIElement {
    public StartMenuLeftVBox() {

        applyStyling(this, "start-menu-left-vbox", "start_menu_left_vbox.css");
        doStuff();

    }


    @Override
    public void doStuff() {
        Text newGameText = new Text("New Game");
        newGameText.getStyleClass().add("start-menu-left-text");
        TextOnlyHBox newGameTextBox = new TextOnlyHBox(newGameText);
        newGameTextBox.setOnMouseClicked(new NewGameEventHandler());
        this.getChildren().add(newGameTextBox);

        Text loadGameText = new Text("Load Game");
        loadGameText.getStyleClass().add("start-menu-left-text");
        TextOnlyHBox loadGameTextBox = new TextOnlyHBox(loadGameText);
        loadGameTextBox.setOnMouseClicked(new LoadGameEventHandler());
        this.getChildren().add(loadGameTextBox);

        Text exitText = new Text("Exit");
        exitText.getStyleClass().add("start-menu-left-text");
        TextOnlyHBox exitTextBox = new TextOnlyHBox(exitText);
        exitTextBox.setOnMouseClicked(new ExitEventHandler());
        this.getChildren().add(exitTextBox);

    }
}
