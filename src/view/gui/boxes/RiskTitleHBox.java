package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class RiskTitleHBox extends HBox implements RiskUIElement {


    public RiskTitleHBox() {
        doStuff();
        applyStyling();
    }

    @Override
    public void applyStyling() {
        this.setAlignment(Pos.CENTER);
    }

    @Override
    public void doStuff() {
        Text text = new Text("Risiko");
        this.getChildren().add(text);
    }
}
