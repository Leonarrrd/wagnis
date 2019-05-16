package view.gui.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import view.gui.helper.RiskUIElement;

import static view.gui.util.UIConstants.WINDOW_HEIGHT;
import static view.gui.util.UIConstants.WINDOW_WIDTH;

public class GameScene extends Scene implements RiskUIElement {
    public GameScene(Parent root) {
        super(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        doStuff();
    }

    @Override
    public void doStuff() {

    }
}
