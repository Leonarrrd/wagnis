package view.gui.boxes.dialogboxes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;
import view.gui.sockets.GameControllerFacade;

import java.io.IOException;

public class MoveVBox extends VBox implements RiskUIElement, Updatable {

    Text moveFromText;
    Text moveToText;
    Spinner<Integer> unitsToMoveWithSpinner;
    Button moveButton;
    boolean firstCountrySelected = false;

    public MoveVBox() {
        applyStyling(this, "move-vbox", "move_vbox.css");
        addAsUpdateElement(this.getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {
        Text moveFromInfoText = new Text("Country to move from");
        moveFromText = new Text("<click on first country>");
        Text moveToInfoText = new Text("Country to move to");
        moveToText = new Text("<click on second country>");

        int maxUnitsToMove = GUIControl.getInstance().getSelectedCountry().getUnits() - 1;

        unitsToMoveWithSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxUnitsToMove, maxUnitsToMove);
        unitsToMoveWithSpinner.setValueFactory(valueFactory);

        moveButton = new Button("Perform move action!");
        moveButton.setDisable(true);

        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(valueFactory.getValue());
                GUIControl.getInstance().move(moveFromText.getText(), moveToText.getText(), unitsToMoveWithSpinner.getValue());
                GUIControl.getInstance().forwardTurnPhase();
            }
        });

        Button skipButton = new Button("Skip");
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().forwardTurnPhase();
            }
        });

        this.getChildren().add(moveFromInfoText);
        this.getChildren().add(moveFromText);
        this.getChildren().add(moveToInfoText);
        this.getChildren().add(moveToText);
        this.getChildren().add(unitsToMoveWithSpinner);
        this.getChildren().add(moveButton);
        this.getChildren().add(skipButton);

    }

    @Override
    public void update() {
        if (!GUIControl.getInstance().getCurrentPlayer().getCountries().containsValue(GUIControl.getInstance().getSelectedCountry())) {
            new Alert(Alert.AlertType.INFORMATION, "You don't own this country.").showAndWait();
        } else {
            if (!firstCountrySelected) {
                if(GUIControl.getInstance().getSelectedCountry().getUnits() == 1) {
                    new Alert(Alert.AlertType.INFORMATION, "Only one unit on this country.").showAndWait();
                    // FIXME: Need to complete these methods first
                } else if (GUIControl.getInstance().hasCountryToMoveTo(GUIControl.getInstance().getSelectedCountry())){
                    new Alert(Alert.AlertType.INFORMATION, "Country is not connected to any of your other countries.").showAndWait();
                } else {
                    // the Spinner does only get updated when the src country gets updated
                    moveFromText.setText(GUIControl.getInstance().getSelectedCountry().getName());
                    int maxUnitsToMove = GUIControl.getInstance().getSelectedCountry().getUnits() - 1;
                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxUnitsToMove, maxUnitsToMove);
                    unitsToMoveWithSpinner.setValueFactory(valueFactory);
                    firstCountrySelected = !firstCountrySelected;
                }
            } else {
                try {
                    if (!GameControllerFacade.getInstance().isConnected(GUIControl.getInstance().getGame().getId(), GUIControl.getInstance().getSelectedCountry(),
                            GUIControl.getInstance().getGame().getCountries().get(moveFromText.getText()))){
                        new Alert(Alert.AlertType.INFORMATION, "Countries are not connected").showAndWait();
                    } else {
                        moveToText.setText(GUIControl.getInstance().getSelectedCountry().getName());
                        firstCountrySelected = !firstCountrySelected;
                        moveButton.setDisable(false);
                    }
                } catch (IOException e) {
                    new ErrorAlert(e);
                }

            }

        }
    }
}
