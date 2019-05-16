package view.gui.boxes;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class StartMenuLeftVBox extends VBox implements RiskUIElement {
    public StartMenuLeftVBox() {
        applyStyling(this, "start-menu-left-vbox", "start_menu_left_vbox.css");
        doStuff();
    }


    @Override
    public void doStuff() {
        this.getStyleClass();
        Text newGameText = new Text("New Game");
        newGameText.getStyleClass().add("start-menu-left-text");
        this.getChildren().add(newGameText);

        getChildren().add(new Text("Element 1"));
        getChildren().add(new Text("Element 1"));
        getChildren().add(new Text("Element 1"));
        getChildren().add(new Text("Element 1"));
        getChildren().add(new Text("Element 1"));
    }
}
