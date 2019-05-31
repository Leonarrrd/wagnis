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

public class PlaceUnitsVBox extends VBox implements RiskUIElement, Updatable {

    Text selectedCountryText;

    public PlaceUnitsVBox() {
        applyStyling(this, "place-units-vbox", "place_units_vbox.css");
        addAsUpdateElement(this.getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {

        Text selectCountryInfoText = new Text("Select Country to place units on");
        selectedCountryText = new Text("<no country selected>");
        final Spinner<Integer> unitsToPlaceSpinner = new Spinner<>();
        final int initialValue = 1;
        //TODO: richtige value setzen
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,5, initialValue);
        unitsToPlaceSpinner.setValueFactory(valueFactory);

        Button placeUnitsButton = new Button("Place Units!");

        placeUnitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().forwardTurnPhase();
            }
        });

        this.getChildren().add(selectCountryInfoText);
        this.getChildren().add(selectedCountryText);
        this.getChildren().add(unitsToPlaceSpinner);
        this.getChildren().add(placeUnitsButton);
    }

    @Override
    public void update() {
        selectedCountryText.setText(GUIControl.getInstance().getSelectedCountry());
    }
}
