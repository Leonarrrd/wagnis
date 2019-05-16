package view.gui.helper;

import javafx.scene.Parent;

public interface RiskUIElement {

    default void applyStyling(Parent parent, String id, String stylesheet) {
        parent.setId(id);
        parent.getStylesheets().add(CSSLoader.loadStyleSheet(this.getClass(), stylesheet));
    }

    void doStuff(); //TODO: methode umbenennen denke ja
}
