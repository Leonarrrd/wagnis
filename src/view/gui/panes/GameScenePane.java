package view.gui.panes;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class GameScenePane extends BorderPane implements RiskUIElement {

    public GameScenePane() {
        doStuff();
    }

    @Override
    public void applyStyling() {

    }

    @Override
    public void doStuff() {
        Text text = new Text("Hello from game scene");
        this.setCenter(text);

    }
}
