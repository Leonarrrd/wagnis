package view.gui.boxes.dialogboxes;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class WaitVBox extends VBox implements RiskUIElement {

    public WaitVBox() {
        applyStyling(this, "perform-another-move-vbox", "perform_another_move_vbox.css");
        init();
    }

    @Override
    public void init() {
        Text text = new Text("Please wait for your turn.");
        this.getChildren().add(text);
    }
}