package view.gui.boxes.dialogboxes;

import datastructures.Phase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;

public class WaitVBox extends VBox implements RiskUIElement {

    public WaitVBox() {
        applyStyling(this, "perform-another-move-vbox", "perform_another_move_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        Text text = new Text("Please wait for your turn.");
        this.getChildren().add(text);
    }
}