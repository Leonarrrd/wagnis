package view.gui.roots;

import javafx.scene.layout.BorderPane;
import view.gui.boxes.RiskTitleHBox;
import view.gui.boxes.StartMenuLeftVBox;
import view.gui.helper.RiskUIElement;
import view.gui.lists.SavedGamesListView;
import view.gui.panes.StartNewGameGridPane;

public class StartBorderPane extends BorderPane implements RiskUIElement, StartMenuLeftVBox.StartActionListener {
    public StartBorderPane() {
        applyStyling(this, "start-border-pane", "start_border_pane.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        this.setTop(new RiskTitleHBox());
        this.setLeft(new StartMenuLeftVBox(this));
    }

    @Override
    public void newGameSelected() {
        this.setCenter(new StartNewGameGridPane());
    }

    @Override
    public void loadGameSelected() {
        this.setCenter(new SavedGamesListView());
    }

    @Override
    public void exitGameSelected() {
        System.exit(0);
    }
}