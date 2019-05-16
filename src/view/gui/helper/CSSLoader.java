package view.gui.helper;

import static view.gui.util.UIConstants.CSS_RESOURCE_PATH;

public class CSSLoader {

    /**
     * loads a stylesheet from /css/-Folder. Call in GUI-Element by:
     * this.getStylesheets().add(CSSLoader.loadStyleSheet(this.getClass(), "<css-file-name>.css"));
     * @param _class
     * @param stylesheet
     * @return
     */
    public static String loadStyleSheet(Class _class, String stylesheet) {
        return _class.getResource(CSS_RESOURCE_PATH + stylesheet).toExternalForm();
    }
}
