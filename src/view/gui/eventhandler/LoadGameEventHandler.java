package view.gui.eventhandler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LoadGameEventHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        System.out.println("loading game");
    }
}
