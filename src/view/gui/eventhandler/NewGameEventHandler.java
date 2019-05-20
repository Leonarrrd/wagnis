package view.gui.eventhandler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import view.gui.Main;
import view.gui.panes.GameBorderPane;
import view.gui.scenes.GameScene;


public class NewGameEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        Main.stage.setScene(new GameScene(new GameBorderPane()));
    }
}
