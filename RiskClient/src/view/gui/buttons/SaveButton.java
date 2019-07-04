package view.gui.buttons;

import datastructures.Phase;
import exceptions.DuplicateGameIdException;
import exceptions.GameNotFoundException;
import javafx.scene.control.Alert;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;

import java.io.IOException;

public class SaveButton extends javafx.scene.control.Button implements RiskUIElement {
    public SaveButton(){
        applyStyling(this, "save-button", "save_button.css");
        init();
    }

    @Override
    public void init() {
        setLayoutX(1370);
        setOnMouseClicked(event -> {
            try {
                String message = "";
                if (GUIControl.getInstance().myTurn() && isLegalPhaseToSave()) {
                    GUIControl.getInstance().saveGame();
                    message = "Game has been saved";
                } else {
                    message = "Not your turn or invalid phase to save";
                }
                new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
            } catch (GameNotFoundException | DuplicateGameIdException | IOException | ClassNotFoundException e){
                new ErrorAlert(e);
            }
        });
    }

    private boolean isLegalPhaseToSave () {
        Phase phase = GUIControl.getInstance().getGame().getTurn().getPhase();
        return phase.equals(Phase.USE_CARDS) || phase.equals(Phase.ATTACK) || phase.equals(Phase.MOVE);
    }
}
