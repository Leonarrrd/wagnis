package view.gui.panes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import view.gui.Main;
import view.gui.boxes.RiskTitleHBox;
import view.gui.boxes.StartMenuLeftVBox;
import view.gui.helper.RiskUIElement;
import view.gui.lists.SavedGamesListView;
import view.gui.scenes.GameScene;

public class StartBorderPane extends BorderPane implements RiskUIElement {
    public StartBorderPane() {
        applyStyling(this, "start-border-pane", "start_border_pane.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        this.setTop(new RiskTitleHBox());
        this.setLeft(new StartMenuLeftVBox());
        this.setCenter(new SavedGamesListView());
        Button button = new Button("Start Game");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.stage.setScene(new GameScene(new GameBorderPane()));
            }
        });
        this.setBottom(button);

    }


}
