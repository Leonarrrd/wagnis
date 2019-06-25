package view.gui.boxes.dialogboxes;

import datastructures.CardSymbol;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.sockets.GameControllerFacade;

public class UseCardsVBox extends VBox implements RiskUIElement {
//    private Spinner oneStarCardSpinner = new Spinner<>();


    public UseCardsVBox() {
        applyStyling(this, "use-cards-vbox", "use_cards_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        Text questionText = new Text("Select cards to use");
        questionText.getStyleClass().add("questionText");
        Text infantryText = new Text("Infantry cards:");
        Text cavalryText = new Text("Cavalry cards:");
        Text artilleryText = new Text("Artillery cards:");

        Player player =GUIControl.getInstance().getPlayer();
        int playerTotalInfantryCards = player.getNumberOfCardsWithSymbol(CardSymbol.INFANTRY);
        int playerTotalCavalryCards = player.getNumberOfCardsWithSymbol(CardSymbol.CAVALRY);
        int playerTotalArtilleryCards = player.getNumberOfCardsWithSymbol(CardSymbol.ARTILLERY);

        Spinner<Integer> infantryCardsSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, playerTotalInfantryCards, 0);
        infantryCardsSpinner.setValueFactory(valueFactory);

        Spinner<Integer> cavalryCardsSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, playerTotalCavalryCards, 0);
        cavalryCardsSpinner.setValueFactory(valueFactory2);

        Spinner<Integer> artilleryCardsSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, playerTotalArtilleryCards, 0);
        artilleryCardsSpinner.setValueFactory(valueFactory3);


        GridPane gp = new GridPane();
        gp.getStyleClass().add("gridpane");
        gp.add(infantryText, 0, 0);
        gp.add(infantryCardsSpinner, 1, 0);
        gp.add(cavalryText, 0, 1);
        gp.add(cavalryCardsSpinner, 1, 1);
        gp.add(artilleryText, 0, 2);
        gp.add(artilleryCardsSpinner, 1, 2);


        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                if (GameControllerFacade.getInstance().getTradeBonusType(infantryCardsSpinner.getValue(), cavalryCardsSpinner.getValue(), artilleryCardsSpinner.getValue()) == null){
                if (!GUIControl.getInstance().isLegalCardUse(infantryCardsSpinner.getValue(), cavalryCardsSpinner.getValue(), artilleryCardsSpinner.getValue())) {
                    new Alert(Alert.AlertType.INFORMATION, "Neither 3 of a kind nor multi.").showAndWait();
                } else {
                    GUIControl.getInstance().useCards(infantryCardsSpinner.getValue(), cavalryCardsSpinner.getValue(), artilleryCardsSpinner.getValue());
                }
            }
        });
        Button skipButton = new Button("Skip");
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().forwardTurnPhase();
            }
        });
        if (player.getCards().size() >= 5){
            skipButton.setDisable(true);
        }

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.getChildren().add(confirmButton);
        hBox.getChildren().add(skipButton);

        this.getChildren().add(questionText);
        this.getChildren().add(gp);
        this.getChildren().add(hBox);
    }
}
