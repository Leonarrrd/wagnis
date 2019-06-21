package view.gui.lists;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import view.gui.alerts.ErrorAlert;
import view.gui.eventhandler.SavedGamesListViewItemClickHandler;
import view.gui.helper.RiskUIElement;
import view.gui.sockets.GameControllerFacade;
import java.io.IOException;

public class SavedGamesListView extends ListView implements RiskUIElement {
    public SavedGamesListView() {
        super();
        applyStyling(this, "saved-games-list-view", "saved_games_list_view.css");
        doStuff();
    }

    @Override
    public void doStuff() {

        try {
            ObservableList<String> gameIds = FXCollections.observableArrayList(GameControllerFacade.getInstance().loadAvailableGameIds());
            this.setItems(gameIds);
            this.setOnMouseClicked(new SavedGamesListViewItemClickHandler(this));
        } catch (IOException | ClassNotFoundException e) {
            new ErrorAlert(e);
        }

    }
}
