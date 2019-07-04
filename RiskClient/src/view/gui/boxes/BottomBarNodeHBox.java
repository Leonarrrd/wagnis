package view.gui.boxes;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import view.gui.helper.CSSLoader;
import view.gui.helper.RiskUIElement;

public abstract class BottomBarNodeHBox extends HBox implements RiskUIElement {
    public BottomBarNodeHBox() {
        applyStyling(this, "bottom-bar-node", "bottom_bar_node.css");
        init();
    }

    @Override
    public void applyStyling(Parent parent, String id, String stylesheet) {
        parent.setId(id);
        parent.getStylesheets().add(CSSLoader.loadStyleSheet(this.getClass(), stylesheet));
        HBox.setHgrow(this, Priority.ALWAYS);
    }

    @Override
    public void init() {

    }
}
