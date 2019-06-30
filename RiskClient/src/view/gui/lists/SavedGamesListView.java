package view.gui.lists;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import view.gui.alerts.ErrorAlert;
import view.gui.eventhandler.SavedGamesListViewItemClickHandler;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;
import view.gui.sockets.GameControllerFacade;
import java.io.IOException;
import java.util.List;

public class SavedGamesListView extends ListView implements RiskUIElement, Updatable {
    public SavedGamesListView() {
        super();
        applyStyling(this, "saved-games-list-view", "saved_games_list_view.css");
        addAsUpdateElement("saved-games-list-view", this);
        doStuff();
    }

    @Override
    public void doStuff() {
        try {
            GameControllerFacade.getInstance().loadAvailableGameIds();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createList(List<String> gamIds){
            ObservableList<String> gameIds = FXCollections.observableArrayList(gamIds);
            this.setItems(gameIds);
            this.setOnMouseClicked(new SavedGamesListViewItemClickHandler(this));
    }

    @Override
    public void update() {

    }
}
