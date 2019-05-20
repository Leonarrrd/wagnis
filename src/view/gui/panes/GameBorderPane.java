package view.gui.panes;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import view.gui.helper.CSSLoader;
import view.gui.helper.RiskUIElement;

public class GameBorderPane extends BorderPane implements RiskUIElement {

    public GameBorderPane() {
        applyStyling(this, "game-border-pane", "game_border_pane.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        Text text = new Text("Hello from game scene");
        this.setCenter(text);

    }
}
