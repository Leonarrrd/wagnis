package view.gui.boxes.dialogboxes;

import datastructures.Phase;
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
import view.gui.sockets.GameControllerFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttackVBox extends VBox implements RiskUIElement, Updatable {

    Text attackingCountryText;
    Text defendingCountryText;
    Spinner<Integer> unitsToAttackWithSpinner;
//    Spinner<Integer> unitsToDefendWithSpinner;
    Button attackButton;
    boolean firstCountrySelected = false;
    GUIControl guic = GUIControl.getInstance();

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
        unitsToAttackWithSpinner = new Spinner<>();
//        Text unitsToDefendWithInfoText = new Text("Choose amount of units to defend with");
//        unitsToDefendWithSpinner = new Spinner<>();

        final int initialValue = 1;
        SpinnerValueFactory<Integer> valueFactoryAtk = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, initialValue);
        SpinnerValueFactory<Integer> valueFactoryDef = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, initialValue);

        unitsToAttackWithSpinner.setValueFactory(valueFactoryAtk);
//        unitsToDefendWithSpinner.setValueFactory(valueFactoryDef);

        attackButton = new Button("Launch Attack!");
        attackButton.setDisable(true);

        attackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                try {
//                    guic.fight(attackingCountryText.getText(), defendingCountryText.getText(), unitsToAttackWithSpinner.getValue(), unitsToDefendWithSpinner.getValue());
//                } catch (GameNotFoundException | NoSuchPlayerException | IOException e) {
//                    e.printStackTrace();
//                }
//                updateSpinners();

                try {
                    guic.initAttack(attackingCountryText.getText(), defendingCountryText.getText(), unitsToAttackWithSpinner.getValue());
                } catch (GameNotFoundException | NoSuchCountryException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button skipButton = new Button("Skip");
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    guic.setTurnManually(Phase.MOVE);
                } catch (NoSuchPlayerException | GameNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.getChildren().add(launchedFromInfoText);
        this.getChildren().add(attackingCountryText);
        this.getChildren().add(attackOnInfoText);
        this.getChildren().add(defendingCountryText);
        this.getChildren().add(unitsToAttackWithInfoText);
        this.getChildren().add(unitsToAttackWithSpinner);
//        this.getChildren().add(unitsToDefendWithInfoText);
//        this.getChildren().add(unitsToDefendWithSpinner);
        this.getChildren().add(attackButton);
        this.getChildren().add(skipButton);

    }

    @Override
    // MARK: refactored to perform all input verifications locally
    //  I left the legacy code in here and commented it out, in case we want to revert for some reason
    public void update() {
        Game game = guic.getGame();
        Player activePlayer = game.getTurn().getPlayer();
//        List<Country> countriesToAttackFrom = new ArrayList<>();
//        try {
//            //TODO: Methodenaufruf zu lang
//            countriesToAttackFrom = new ArrayList(GameControllerFacade.getInstance().getCountriesAttackCanBeLaunchedFrom(game.getId(), activePlayer).values());
//        } catch (GameNotFoundException | NoSuchPlayerException | NoSuchCountryException | IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        if (!firstCountrySelected) {
            if (!activePlayer.getCountries().values().contains(guic.getSelectedCountry())) {
                new Alert(Alert.AlertType.INFORMATION, "You do not own " + guic.getSelectedCountry().getName()).showAndWait();

            } else if (guic.getSelectedCountry().getUnits() <= 1) {
                new Alert(Alert.AlertType.INFORMATION, "Only one unit on " + guic.getSelectedCountry().getName()).showAndWait();
//            } else if (!countriesToAttackFrom.contains(guic.getSelectedCountry())) {
            } else if (!guic.hasHostileNeighbors(guic.getSelectedCountry())) {
                new Alert(Alert.AlertType.INFORMATION,  guic.getSelectedCountry().getName()+ " has no hostile neighbours").showAndWait();
            } else {
                attackingCountryText.setText(guic.getSelectedCountry().getName());
                firstCountrySelected = !firstCountrySelected;
            }
        } else {
            Country firstCountry = game.getCountries().get(attackingCountryText.getText());
//            try {
//                if(firstCountry.getNeighbors().contains(game.getCountries().get(guic.getSelectedCountry().getName()))
//                    && GameControllerFacade.getInstance().getHostileNeighbors(game.getId(), firstCountry).containsKey(guic.getSelectedCountry().getName())) {
                if (firstCountry.getNeighbors().contains(guic.getSelectedCountry()) && !firstCountry.getOwner().equals(guic.getSelectedCountry().getOwner())){
                defendingCountryText.setText(guic.getSelectedCountry().getName());
                    firstCountrySelected = !firstCountrySelected;
                    attackButton.setDisable(false);

                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Countries not adjacent or not hostile.").showAndWait();
                }
//            } catch (GameNotFoundException | NoSuchCountryException | IOException | ClassNotFoundException e) {
//                new ErrorAlert(e);
//            }
        }

        updateSpinners();
    }

    void updateSpinners(){
        Game game = guic.getGame();
        if (game.getTurn().getPhase().equals(Phase.ATTACK)) {
            if (game.getCountries().get(attackingCountryText.getText()) != null) {
                int maxAttackers;
                if (game.getCountries().get(attackingCountryText.getText()).getUnits() > 3) {
                    maxAttackers = 3;
                } else {
                    maxAttackers = game.getCountries().get(attackingCountryText.getText()).getUnits() - 1;
                }
                SpinnerValueFactory<Integer> valueFactoryAtk = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxAttackers, maxAttackers);
                unitsToAttackWithSpinner.setValueFactory(valueFactoryAtk);
            }

//            if (game.getCountries().get(defendingCountryText.getText()) != null) {
//                int maxDefenders;
//                if (game.getCountries().get(defendingCountryText.getText()).getUnits() > 2) {
//                    maxDefenders = 2;
//                } else {
//                    maxDefenders = game.getCountries().get(defendingCountryText.getText()).getUnits();
//                }
//
//                SpinnerValueFactory<Integer> valueFactoryDef = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxDefenders, maxDefenders);
//                unitsToDefendWithSpinner.setValueFactory(valueFactoryDef);
//            }
        }
    }
}
