package view.gui.boxes;

import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class PlayerListHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {
    public PlayerListHBox (){
        super();
        addAsUpdateElement("player-list-hbox", this);
    }

    @Override
    public void doStuff() {
        Text text = new Text("Pumuckl\nMütze\nGrottenolm");
        this.getChildren().add(text);
    }

    @Override
    public void update() {

    }
}
