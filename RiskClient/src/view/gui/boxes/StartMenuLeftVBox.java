package view.gui.boxes;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class StartMenuLeftVBox extends VBox implements RiskUIElement {

    public interface StartActionListener {
        void newGameSelected();
        void joinGameSelected();
        void loadGameSelected();
        void exitGameSelected();
    }

    private StartActionListener startActionListener;

    public StartMenuLeftVBox(StartActionListener listener) {
        this.startActionListener = listener;
        applyStyling(this, "start-menu-left-vbox", "start_menu_left_vbox.css");
        doStuff();
    }


    @Override
    public void doStuff() {
        Text newGameText = new Text("New Game");
        newGameText.getStyleClass().add("start-menu-left-text");
        TextOnlyHBox newGameTextBox = new TextOnlyHBox(newGameText);
//        newGameTextBox.setOnMouseClicked(new NewGameEventHandler());
        newGameTextBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startActionListener.newGameSelected();
            }
        });
        this.getChildren().add(newGameTextBox);

        Text joinGameText = new Text("Join Game");
        joinGameText.getStyleClass().add("start-menu-left-text");
        TextOnlyHBox joinGameTextBox = new TextOnlyHBox(joinGameText);
        joinGameText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startActionListener.joinGameSelected();
            }
        });
        this.getChildren().add(joinGameTextBox);

        Text loadGameText = new Text("Load Game");
        loadGameText.getStyleClass().add("start-menu-left-text");
        TextOnlyHBox loadGameTextBox = new TextOnlyHBox(loadGameText);
        loadGameTextBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startActionListener.loadGameSelected();
            }
        });
        this.getChildren().add(loadGameTextBox);

        Text exitText = new Text("Exit");
        exitText.getStyleClass().add("start-menu-left-text");
        TextOnlyHBox exitTextBox = new TextOnlyHBox(exitText);
        exitTextBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startActionListener.exitGameSelected();
            }
        });
        this.getChildren().add(exitTextBox);

    }
}
