package view.gui.lists;

import controller.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import model.Game;
import persistence.FileReader;
import view.gui.alerts.ErrorAlert;
import view.gui.eventhandler.SavedGamesListViewItemClickHandler;
import view.gui.helper.RiskUIElement;

import javax.swing.text.Element;
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
            ObservableList<String> gameIds = FXCollections.observableArrayList(FileReader.getInstance().loadAvailableGameIds());
            this.setItems(gameIds);
            this.setOnMouseClicked(new SavedGamesListViewItemClickHandler(this));
        } catch (IOException e) {
            new ErrorAlert(Alert.AlertType.ERROR,"Exception occured", "Stacktrace: ", e);
        }

    }
}
