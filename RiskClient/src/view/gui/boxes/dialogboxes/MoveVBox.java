package view.gui.boxes.dialogboxes;

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
    Button moveButton;
    boolean firstCountrySelected = false;

    public MoveVBox() {
        applyStyling(this, "move-vbox", "move_vbox.css");
        addAsUpdateElement(this.getId(), this);
        init();
    }

    @Override
    public void init() {
        Text moveFromInfoText = new Text("Country to move from");
        moveFromText = new Text("<click on first country>");
        Text moveToInfoText = new Text("Country to move to");
        moveToText = new Text("<click on second country>");

        int maxUnitsToMove = GUIControl.getInstance().getSelectedCountry().getUnits() - 1;

        //hacky fix
        if(maxUnitsToMove < 1) {
            maxUnitsToMove = 1;
        }

        unitsToMoveWithSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxUnitsToMove, maxUnitsToMove);
        unitsToMoveWithSpinner.setValueFactory(valueFactory);

        moveButton = new Button("Perform move action!");
        moveButton.setDisable(true);

        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().move(moveFromText.getText(), moveToText.getText(), unitsToMoveWithSpinner.getValue());
            }
        });

        Button skipButton = new Button("Skip");
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().nextPlayerTurn();
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
        GUIControl guic = GUIControl.getInstance();
        if (!guic.getCurrentPlayer().getCountries().containsValue(guic.getSelectedCountry())) {
            new Alert(Alert.AlertType.INFORMATION, "You don't own this country.").showAndWait();
        } else {
            if (!firstCountrySelected) {
                moveToText.setText("<Country to move to>");
                moveButton.setDisable(true);
                if(guic.getSelectedCountry().getUnits() == 1) {
                    new Alert(Alert.AlertType.INFORMATION, "Only one unit on this country.").showAndWait();
                } else {
                    if (guic.getCurrentPlayer().getCountryGraph().evaluateCountriesAllowedToMoveTo(guic.getSelectedCountry().getName()).size() == 1){
                        new Alert(Alert.AlertType.INFORMATION, "Country is not connected to any of your other countries.").showAndWait();
                    } else {
                        // the Spinner does only get updated when the src country gets updated
                        moveFromText.setText(guic.getSelectedCountry().getName());
                        int maxUnitsToMove = guic.getSelectedCountry().getUnits() - 1;
                        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxUnitsToMove, maxUnitsToMove);
                        unitsToMoveWithSpinner.setValueFactory(valueFactory);
                        firstCountrySelected = !firstCountrySelected;
                    }
                }
            } else {
                if(!guic.getCurrentPlayer().getCountryGraph().isConnected(guic.getSelectedCountry(), guic.getGame().getCountries().get(moveFromText.getText()))){
                    moveFromText.setText("<Country to move from>");
                    moveToText.setText("<Country to move to>");
                    firstCountrySelected = false;
                    new Alert(Alert.AlertType.INFORMATION, "Countries are not connected").showAndWait();
                } else {
                    moveToText.setText(guic.getSelectedCountry().getName());
                    firstCountrySelected = !firstCountrySelected;
                    moveButton.setDisable(false);
                }
            }

        }
    }
}
