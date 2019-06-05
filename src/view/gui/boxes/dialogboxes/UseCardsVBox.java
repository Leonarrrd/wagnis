package view.gui.boxes.dialogboxes;

import controller.GameController;
import datastructures.Phase;
import exceptions.GameNotFoundException;
import exceptions.NoSuchPlayerException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;

import java.util.UUID;

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
        Text oneStarCardText = new Text("One star cards:");
        Text twoStarCardText = new Text("Two star cards:");

        Player player = GUIControl.getInstance().getGame().getTurn().getPlayer();
        int playerTotalOneStarCards = player.getNumberOfCardsWithXStars(1);
        int playerTotalTwoStarCards = player.getNumberOfCardsWithXStars(2);

        Spinner<Integer> oneStarCardSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, playerTotalOneStarCards, 0);
        oneStarCardSpinner.setValueFactory(valueFactory);

        Spinner<Integer> twoStarCardSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, playerTotalTwoStarCards, 0);
        twoStarCardSpinner.setValueFactory(valueFactory2);


        GridPane gp = new GridPane();
        gp.getStyleClass().add("gridpane");
        gp.add(oneStarCardText, 0, 0);
        gp.add(oneStarCardSpinner, 1, 0);
        gp.add(twoStarCardText, 0, 1);
        gp.add(twoStarCardSpinner, 1, 1);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().useCards(oneStarCardSpinner.getValue(), twoStarCardSpinner.getValue());
                GUIControl.getInstance().forwardTurnPhase();
            }
        });


        this.getChildren().add(questionText);
        this.getChildren().add(gp);
        this.getChildren().add(confirmButton);
    }
}
