package view.gui.boxes;

import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class CardsHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {
    public CardsHBox() {
        super();
        addAsUpdateElement("cards-hbox", this);
    }

    @Override
    public void doStuff() {
        Text text = new Text("Coole Karten");
        this.getChildren().add(text);
    }

    @Override
    public void update() {

    }
}
