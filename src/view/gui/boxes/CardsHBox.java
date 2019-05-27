package view.gui.boxes;

import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class CardsHBox extends BottomBarNodeHBox implements RiskUIElement {
    public CardsHBox() {
        super();
    }

    @Override
    public void doStuff() {
        Text text = new Text("Coole Karten");
        this.getChildren().add(text);
    }
}
