package view.gui.lists;

import controller.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.Game;
import persistence.FileReader;
import view.gui.helper.RiskUIElement;

import javax.swing.text.Element;
import java.io.IOException;

public class SavedGamesListView extends ListView implements RiskUIElement {
    public SavedGamesListView() {
        super();
        doStuff();
    }


    @Override
    public void applyStyling() {

    }

    @Override
    public void doStuff() {

        try {
            ObservableList<String> gameIds = FXCollections.observableArrayList(FileReader.getInstance().loadAvailableGameIds());
            this.setItems(gameIds);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
