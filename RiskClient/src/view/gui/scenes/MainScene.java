package view.gui.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import view.gui.helper.RiskUIElement;

import static view.gui.util.UIConstants.WINDOW_WIDTH;
import static view.gui.util.UIConstants.WINDOW_HEIGHT;

public class MainScene extends Scene implements RiskUIElement {
    public MainScene(Parent root) {
        super(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        init();
    }

    @Override
    public void init() {
    }
}
