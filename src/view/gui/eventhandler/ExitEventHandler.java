package view.gui.eventhandler;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ExitEventHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        System.exit(0);
    }
}
