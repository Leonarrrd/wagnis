package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class CardsHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {
    private Text text;
    public CardsHBox() {
        super();
        addAsUpdateElement("cards-hbox", this);

    }

    @Override
    public void doStuff() {
        this.text = new Text("Total: " + GUIControl.getInstance().getGame().getTurn().getPlayer().getCards().size() + " cards");

        VBox vBox = new VBox();
        vBox.getChildren().add(new Text("Your cards:"));
        vBox.getChildren().add(text);
        this.getChildren().add(vBox);
    }

    @Override
    public void update() {

    }
}
