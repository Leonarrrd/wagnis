package view.gui.boxes;

import javafx.scene.layout.VBox;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class DiceVBox extends VBox implements RiskUIElement, Updatable {

    public DiceVBox() {
        applyStyling(this, "dice-vbox", "dice_vbox.css");
        addAsUpdateElement(this.getId(), this);
    }

    @Override
    public void doStuff() {

    }

    @Override
    public void update() {

    }
}
