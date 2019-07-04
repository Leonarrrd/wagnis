package view.gui.helper;

import javafx.scene.Parent;

public interface RiskUIElement {

    /**
     * Gives the component and Id and binds an css-Sheet to the component
     * so that the styling infomration can be done by css only.
     * Needs to be called in the constructor and provided an id.
     * @param parent
     * @param id
     * @param stylesheet
     */
    default void applyStyling(Parent parent, String id, String stylesheet) {
        parent.setId(id);
        parent.getStylesheets().add(CSSLoader.loadStyleSheet(this.getClass(), stylesheet));
    }

    /**
     * Initializes the components held by this GUI-Element
     */
    void init();
}
