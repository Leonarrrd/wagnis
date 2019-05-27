package view.gui.boxes;

import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class PlayerListHBox extends BottomBarNodeHBox implements RiskUIElement {
    public PlayerListHBox (){
        super();
    }

    @Override
    public void doStuff() {
        Text text = new Text("Pumuckl\nMütze\nGrottenolm");
        this.getChildren().add(text);
    }
}
