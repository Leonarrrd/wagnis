package view.gui.boxes.dialogboxes;

import controller.GameController;
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

import java.util.UUID;

public class AttackVBox extends VBox implements RiskUIElement, Updatable {

    Text attackingCountryText;
    Text defendingCountryText;
    boolean firstCountrySelected = false;

    public AttackVBox() {
        applyStyling(this, "attack-vbox", "attack_vbox.css");
        addAsUpdateElement(this.getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {
        Text launchedFromInfoText = new Text("Attack launched from");
        attackingCountryText = new Text("<click on first country>");
        Text attackOnInfoText = new Text("Attack on");
        defendingCountryText = new Text("<click on second country>");
        Text unitsToAttackWithInfoText = new Text("Choose amount of units to attack with");
        final Spinner<Integer> unitsToAttackWithSpinner = new Spinner<>();

        final int initialValue = 1;

        //TODO: richtige value setzen
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,5, initialValue);
        unitsToAttackWithSpinner.setValueFactory(valueFactory);
        Button attackButton = new Button("Launch Attack!");

        attackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().fight(attackingCountryText.getText(), defendingCountryText.getText(), unitsToAttackWithSpinner.getValue(), unitsToAttackWithSpinner.getValue());
            }
        });

        this.getChildren().add(launchedFromInfoText);
        this.getChildren().add(attackingCountryText);
        this.getChildren().add(attackOnInfoText);
        this.getChildren().add(defendingCountryText);
        this.getChildren().add(unitsToAttackWithInfoText);
        this.getChildren().add(unitsToAttackWithSpinner);
        this.getChildren().add(attackButton);

    }

    @Override
    public void update() {
        if(!firstCountrySelected) {
            attackingCountryText.setText(GUIControl.getInstance().getSelectedCountry());
        } else {
            defendingCountryText.setText(GUIControl.getInstance().getSelectedCountry());
        }
        firstCountrySelected  = !firstCountrySelected;
    }
}
