package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class RiskTitleHBox extends HBox implements RiskUIElement {


    public RiskTitleHBox() {
        applyStyling(this, "risk-title-hbox", "risk_title_hbox.css");
        doStuff();
    }


    @Override
    public void doStuff() {
        Text title = new Text("Risiko");
        title.getStyleClass().add("risk-title");

        this.getChildren().add(title);
    }
}
