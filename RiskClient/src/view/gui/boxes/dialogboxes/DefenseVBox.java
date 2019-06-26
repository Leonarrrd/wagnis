package view.gui.boxes.dialogboxes;

import datastructures.Phase;
import exceptions.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.AttackResult;
import model.Country;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.sockets.GameControllerFacade;

import java.io.IOException;

public class DefenseVBox extends VBox implements RiskUIElement {
    private GUIControl guic = GUIControl.getInstance();
    private Country attackingCountry;
    private Country defendingCountry;
    private int attackingUnits;

    public DefenseVBox(String attackingCountryString, String defendingCountryString, String unitsString) {
        applyStyling(this, "defense-vbox", "defense_vbox.css");
        this.attackingCountry = guic.getGame().getCountries().get(attackingCountryString);
        this.defendingCountry = guic.getGame().getCountries().get(defendingCountryString);
        this.attackingUnits = Integer.parseInt(unitsString);
        doStuff();
    }

    @Override
    public void doStuff() {
        Text infoText = new Text(attackingCountry.getOwner().getName() + " is attacking you in "
                + defendingCountry.getName() + " from " + attackingCountry.getName() + "!"
                + "\nHow many units do you want to defend with?");

        //TODO: maximale units anzeigen
        int maxValue = defendingCountry.getUnits() < 2 ? 1 : 2;

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxValue, maxValue);
        Spinner<Integer> defendingUnitsSpinner = new Spinner<>();
        defendingUnitsSpinner.setValueFactory(valueFactory);

        Button defendButton = new Button("Defend!");
        defendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int defendingUnits = defendingUnitsSpinner.getValue();
                try {
                    AttackResult ar = GameControllerFacade.getInstance().fight(null, attackingCountry, defendingCountry, attackingUnits, defendingUnits);
                    //GUIControl.getInstance().fight(attackingCountry, defendingCountry, ar);
                   // GUIControl.getInstance().forwardTurnPhase();
                } catch (IOException e) {
                    //this is fine

                } catch (GameNotFoundException | ClassNotFoundException | NoSuchCountryException | NotEnoughUnitsException | CountriesNotAdjacentException e) {
                    new ErrorAlert(e);
                }
            }
        });


        this.getChildren().add(infoText);
        this.getChildren().add(defendingUnitsSpinner);
        this.getChildren().add(defendButton);
    }
}
