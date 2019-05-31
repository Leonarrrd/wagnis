package view.gui.boxes.dialogboxes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

        //TODO: richtige value setzen
        final Spinner<Integer> unitsToMoveWithSpinner = new Spinner<>();
        final int initialValue = 1;
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, initialValue);
        unitsToMoveWithSpinner.setValueFactory(valueFactory);

        Button moveButton = new Button("Perform move action!");
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
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

    }

    @Override
    public void update() {
        if (!firstCountrySelected) {
            moveFromText.setText(GUIControl.getInstance().getSelectedCountry());
        } else {
            moveToText.setText(GUIControl.getInstance().getSelectedCountry());
        }

        firstCountrySelected = !firstCountrySelected;

    }
}
