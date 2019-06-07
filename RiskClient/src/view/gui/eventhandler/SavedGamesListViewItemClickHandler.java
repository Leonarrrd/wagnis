package view.gui.eventhandler;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Game;
import view.gui.helper.GUIControl;
import view.gui.roots.GameBorderPane;

import java.util.UUID;

public class SavedGamesListViewItemClickHandler implements EventHandler<MouseEvent> {

    private ListView listView;
    public SavedGamesListViewItemClickHandler(ListView listView) {
        this.listView = listView;
    }

    @Override
    public void handle(MouseEvent event) {
        String gameIdString = (String) this.listView.getSelectionModel().getSelectedItem();
        UUID gameId = UUID.fromString(gameIdString);
        GUIControl.getInstance().initLoadedGame(gameId);

        // kinda weird to access the scene like this?
        listView.getScene().setRoot(new GameBorderPane());
    }
}
