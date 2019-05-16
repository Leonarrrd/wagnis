package view.gui.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import view.gui.helper.RiskUIElement;

import static view.gui.util.UIConstants.WINDOW_WIDTH;
import static view.gui.util.UIConstants.WINDOW_HEIGHT;

public class StartScene extends Scene implements RiskUIElement {
    public StartScene(Parent root) {
        super(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        doStuff();
    }

    @Override
    public void doStuff() {
    }
}
