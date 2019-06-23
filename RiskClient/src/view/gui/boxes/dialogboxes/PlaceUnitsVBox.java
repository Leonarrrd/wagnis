package view.gui.boxes.dialogboxes;

import exceptions.GameNotFoundException;
import exceptions.NoSuchCountryException;
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

import java.io.IOException;
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

        //TODO: maximale units anzeigen
        int maxValue = GUIControl.getInstance().getGame().getTurn().getPlayer().getUnitsToPlace();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxValue, maxValue);
        unitsToPlaceSpinner.setValueFactory(valueFactory);

        placeUnitsButton = new Button("Place Units!");
        placeUnitsButton.setDisable(true);

        placeUnitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    GUIControl.getInstance().placeUnits(unitsToPlaceSpinner.getValue());
                } catch (GameNotFoundException | NoSuchCountryException | IOException e) {
                    e.printStackTrace();
                }
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

        int maxValue = GUIControl.getInstance().getGame().getTurn().getPlayer().getUnitsToPlace();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxValue, maxValue);
        unitsToPlaceSpinner.setValueFactory(valueFactory);

        List<Country> playerCountries = new ArrayList(GUIControl.getInstance().getCurrentPlayer().getCountries().values());
//        Country selectedCountry = GUIControl.getInstance().getGame().getCountries().get(GUIControl.getInstance().getSelectedCountry());
        Country selectedCountry = GUIControl.getInstance().getSelectedCountry();

        if (playerCountries.contains(selectedCountry)) {
            selectedCountryText.setText(GUIControl.getInstance().getSelectedCountry().getName());
            placeUnitsButton.setDisable(false);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "You don't own this country.").showAndWait();
            placeUnitsButton.setDisable(true);
            selectedCountryText.setText("<no country selected>");
        }
    }
}
