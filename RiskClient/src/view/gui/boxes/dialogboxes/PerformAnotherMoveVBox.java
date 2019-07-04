package view.gui.boxes.dialogboxes;

import datastructures.Phase;
import exceptions.CardAlreadyOwnedException;
import exceptions.GameNotFoundException;
import exceptions.NoSuchCardException;
import exceptions.NoSuchPlayerException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;

import java.io.IOException;

public class PerformAnotherMoveVBox extends VBox implements RiskUIElement {

    public PerformAnotherMoveVBox() {
        applyStyling(this, "perform-another-move-vbox", "perform_another_move_vbox.css");
        init();
    }

    @Override
    public void init() {

        HBox answers = new HBox();
        Text text = new Text("Do you want to perform another move operation?");

        answers.setAlignment(Pos.CENTER);
        answers.setPadding(new Insets(50,0,0,0));
        answers.setSpacing(30);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    GUIControl.getInstance().setTurnManually(Phase.MOVE);
                } catch (NoSuchPlayerException | GameNotFoundException | IOException | NoSuchCardException | CardAlreadyOwnedException e) {
                    new ErrorAlert(e);
                }

            }
        });

        Button noButton = new Button("No");
        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                GUIControl.getInstance().nextPlayerTurn();
            }
        });

        answers.getChildren().add(yesButton);
        answers.getChildren().add(noButton);



        this.getChildren().add(text);
        this.getChildren().add(answers);

    }
}
