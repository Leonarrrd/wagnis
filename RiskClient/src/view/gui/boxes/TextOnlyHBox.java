package view.gui.boxes;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class TextOnlyHBox extends HBox implements RiskUIElement {

    private Text text;
    public TextOnlyHBox(Text text) {
        this.text = text;
        applyStyling(this, "text-only-hbox", "text_only_hbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        getChildren().add(this.text);
    }
}
