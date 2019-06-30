package view.gui.eventhandler;

import exceptions.GameNotFoundException;
import exceptions.InvalidFormattedDataException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import model.Game;
import view.gui.helper.GUIControl;
import view.gui.panes.StartLoadedGameGridPane;
import view.gui.roots.GameBorderPane;
import view.gui.roots.StartBorderPane;
import view.gui.sockets.GameControllerFacade;

import java.io.IOException;
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
        try {
            GameControllerFacade.getInstance().loadGame(gameId);
        } catch (IOException | GameNotFoundException | InvalidFormattedDataException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        StartBorderPane sbp = (StartBorderPane) listView.getParent();
        sbp.setCenter(new StartLoadedGameGridPane(gameId));
    }
}
