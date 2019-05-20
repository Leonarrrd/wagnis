package view.gui.eventhandler;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class SavedGamesListViewItemClickHandler implements EventHandler<MouseEvent> {

    private ListView listView;
    public SavedGamesListViewItemClickHandler(ListView listView) {
        this.listView = listView;
    }

    @Override
    public void handle(MouseEvent event) {
        String s = (String) this.listView.getSelectionModel().getSelectedItem();
        System.out.println(s);
    }
}
