package view.gui.buttons;

import exceptions.DuplicateGameIdException;
import exceptions.GameNotFoundException;
import javafx.scene.control.ToggleButton;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;

import java.io.IOException;

public class SaveButton extends javafx.scene.control.Button implements RiskUIElement {
    public SaveButton(){
        applyStyling(this, "save-button", "save_button.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        setLayoutX(1370);
        setOnMouseClicked(event -> {
            try {
                GUIControl.getInstance().saveGame();
            } catch (GameNotFoundException | DuplicateGameIdException | IOException e){
                e.printStackTrace();
            }
        });
    }
}
