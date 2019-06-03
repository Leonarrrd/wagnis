package view.gui.boxes.dialogboxes;

import controller.GameController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class MoveVBox extends VBox implements RiskUIElement, Updatable {

    Text moveFromText;
    Text moveToText;
    Spinner<Integer> unitsToMoveWithSpinner;
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

        Button moveButton = new Button("Perform move action!");
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
        // MARK: I made it so that the VBox does not update when the player selects a country that's not his
        //  --> This way, we don't have to deal with the player trying
        //      to perform move operations on countries that aren't his
        if (GUIControl.getInstance().getCurrentPlayer().getCountries().containsValue(GUIControl.getInstance().getSelectedCountry())) {
            if (!firstCountrySelected) {
                // the Spinner does only get updated when the src country gets updated
                moveFromText.setText(GUIControl.getInstance().getSelectedCountry().getName());
                int maxUnitsToMove = GUIControl.getInstance().getSelectedCountry().getUnits() - 1;
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxUnitsToMove, maxUnitsToMove);
                unitsToMoveWithSpinner.setValueFactory(valueFactory);
            } else {
                moveToText.setText(GUIControl.getInstance().getSelectedCountry().getName());
            }
            firstCountrySelected = !firstCountrySelected;
        } else {
            new Alert(Alert.AlertType.INFORMATION, "You don't own this country.").showAndWait();
        }
    }
}
