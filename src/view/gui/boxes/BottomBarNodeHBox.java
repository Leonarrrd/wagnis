package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public abstract class BottomBarNodeHBox extends HBox implements RiskUIElement {
    public BottomBarNodeHBox() {
        applyStyling(this, "bottom-bar-node", "bottom_bar_node.css");
        doStuff();

        this.setAlignment(Pos.CENTER);
        this.setMaxWidth(400);
//        this.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);
    }


    @Override
    public void doStuff() {

    }
}
