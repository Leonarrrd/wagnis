package view.gui.boxes.dialogboxes;

import controller.GameController;
import exceptions.GameNotFoundException;
import exceptions.NoSuchCountryException;
import exceptions.NoSuchPlayerException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Country;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

import java.util.ArrayList;
import java.util.List;
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
        Text unitsToDefendWithInfoText = new Text("Choose amount of units to defend with");
        final Spinner<Integer> unitsToDefendWithSpinner = new Spinner<>();

        final int initialValue = 1;

        //TODO: richtige value setzen
        SpinnerValueFactory<Integer> valueFactoryAtk = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, initialValue);
        SpinnerValueFactory<Integer> valueFactoryDef = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, initialValue);

        unitsToAttackWithSpinner.setValueFactory(valueFactoryAtk);
        unitsToDefendWithSpinner.setValueFactory(valueFactoryDef);

        Button attackButton = new Button("Launch Attack!");

        attackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().fight(attackingCountryText.getText(), defendingCountryText.getText(), unitsToAttackWithSpinner.getValue(), unitsToDefendWithSpinner.getValue());
            }
        });

        Button skipButton = new Button("Skip");
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().forwardTurnPhase();
            }
        });

        this.getChildren().add(launchedFromInfoText);
        this.getChildren().add(attackingCountryText);
        this.getChildren().add(attackOnInfoText);
        this.getChildren().add(defendingCountryText);
        this.getChildren().add(unitsToAttackWithInfoText);
        this.getChildren().add(unitsToAttackWithSpinner);
        this.getChildren().add(unitsToDefendWithInfoText);
        this.getChildren().add(unitsToDefendWithSpinner);
        this.getChildren().add(attackButton);
        this.getChildren().add(skipButton);

    }

    @Override
    public void update() {

        Game game = GUIControl.getInstance().getGame();
        Player activePlayer = game.getTurn().getPlayer();
        List<Country> countriesToAttackFrom;
        try {
            //TODO: Methodenaufruf zu lang
            countriesToAttackFrom = new ArrayList(GameController.getInstance().getCountriesAttackCanBeLaunchedFrom(game.getId(), activePlayer).values());
        } catch (GameNotFoundException | NoSuchPlayerException | NoSuchCountryException e) {
            e.printStackTrace();
        }
        if (!firstCountrySelected) {
            if (activePlayer.getCountries().keySet().contains(GUIControl.getInstance().getSelectedCountry())) {
                attackingCountryText.setText(GUIControl.getInstance().getSelectedCountry());
                firstCountrySelected = !firstCountrySelected;

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Player does not own " + GUIControl.getInstance().getSelectedCountry()).showAndWait();
            }
        } else {
            Country firstCountry = game.getCountries().get(attackingCountryText.getText());
            try {
                if(firstCountry.getNeighbors().contains(game.getCountries().get(GUIControl.getInstance().getSelectedCountry()))
                    && GameController.getInstance().getHostileNeighbors(game.getId(), firstCountry).containsKey(GUIControl.getInstance().getSelectedCountry())) {
                defendingCountryText.setText(GUIControl.getInstance().getSelectedCountry());
                    firstCountrySelected = !firstCountrySelected;

                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Countries not adjacent or not hostile.").showAndWait();
                }
            } catch (GameNotFoundException | NoSuchCountryException e) {
                new ErrorAlert(e);
            }
        }

    }
}
