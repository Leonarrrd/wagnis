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
import model.Country;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

import java.util.ArrayList;
import java.util.List;

public class PlaceUnitsVBox extends VBox implements RiskUIElement, Updatable {

    Text selectedCountryText;
    Button placeUnitsButton;
    Spinner<Integer> unitsToPlaceSpinner;

    public PlaceUnitsVBox() {
        applyStyling(this, "place-units-vbox", "place_units_vbox.css");
        addAsUpdateElement(this.getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {

        Text selectCountryInfoText = new Text("Select Country to place units on");
        selectedCountryText = new Text("<no country selected>");
        unitsToPlaceSpinner = new Spinner<>();
        final int initialValue = 54;
        //TODO: richtige value setzen
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 55, initialValue);
        unitsToPlaceSpinner.setValueFactory(valueFactory);

        placeUnitsButton = new Button("Place Units!");
        placeUnitsButton.setDisable(true);

        placeUnitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().placeUnits(unitsToPlaceSpinner.getValue());
            }
        });

        this.getChildren().add(selectCountryInfoText);
        this.getChildren().add(selectedCountryText);
        this.getChildren().add(unitsToPlaceSpinner);
        this.getChildren().add(placeUnitsButton);
    }

    @Override
    public void update() {

        //TODO: oooooofff funktionsaufrufe
        List<Country> playerCountries = new ArrayList(GUIControl.getInstance().getGame().getTurn().getPlayer().getCountries().values());
        Country selectedCountry = GUIControl.getInstance().getGame().getCountries().get(GUIControl.getInstance().getSelectedCountry());

        if (playerCountries.contains(selectedCountry)) {
            selectedCountryText.setText(GUIControl.getInstance().getSelectedCountry());
            placeUnitsButton.setDisable(false);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Please select a country that belongs to you.").showAndWait();
        }


    }
}
