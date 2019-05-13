package view.gui.panes;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import view.gui.Main;
import view.gui.boxes.RiskTitleHBox;
import view.gui.helper.RiskUIElement;
import view.gui.lists.SavedGamesListView;
import view.gui.scenes.GameScene;

public class StartBorderPane extends BorderPane implements RiskUIElement {
    public StartBorderPane() {
        doStuff();
    }


    @Override
    public void applyStyling() {
    }

    @Override
    public void doStuff() {
        this.setTop(new RiskTitleHBox());
        Text leftNode = new Text("leftNode");
        this.setLeft(leftNode);
        this.setCenter(new SavedGamesListView());
        Button button = new Button("Start Game");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.stage.setScene(new GameScene(new GameScenePane()));
            }
        });
        this.setBottom(button);

    }
}
