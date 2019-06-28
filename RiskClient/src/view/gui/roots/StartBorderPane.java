package view.gui.roots;

import helper.Utils;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.JoinGameVBox;
import view.gui.boxes.RiskTitleHBox;
import view.gui.boxes.StartMenuLeftVBox;
import view.gui.helper.RiskUIElement;
import view.gui.lists.SavedGamesListView;
import view.gui.panes.StartNewGameGridPane;

import java.util.Optional;

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
        TextInputDialog textInputDialog = new TextInputDialog("Player 1");
        textInputDialog.setTitle("Create new name.");
        textInputDialog.setHeaderText("Please choose a name.");
        textInputDialog.setContentText("Please enter your name: ");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent())
            if (!Utils.stringContainsDelimitters(result.get())) {
                this.setCenter(new StartNewGameGridPane(result.get()));
            } else {
                new ErrorAlert("Invalid Player Name", "Please choose another name", "Please do not use , ' - : in your name.");

            }

    }

    @Override
    public void joinGameSelected() {
        this.setCenter(new JoinGameVBox());
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