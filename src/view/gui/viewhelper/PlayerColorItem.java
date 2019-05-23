package view.gui.viewhelper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PlayerColorItem {
    private TextField playerTextField;
    private ComboBox playerComboBox;

    public PlayerColorItem(TextField playerTextField, ComboBox playerComboBox) {
        this.playerTextField = playerTextField;
        this.playerComboBox = playerComboBox;
    }

    public TextField getPlayerTextField() {
        return playerTextField;
    }

    public ComboBox getPlayerComboBox() {
        return playerComboBox;
    }
}
