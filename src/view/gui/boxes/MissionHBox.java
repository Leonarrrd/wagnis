package view.gui.boxes;

import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class MissionHBox extends BottomBarNodeHBox implements RiskUIElement {
    public MissionHBox() {
        super();
    }

    @Override
    public void doStuff() {
        this.setMaxWidth(300);

        Text text = new Text("Conquer 30 countries and destroy Philipps Gesicht");
        text.setWrappingWidth(200);
        this.getChildren().add(text);
    }
}
