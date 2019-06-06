package view.gui.boxes.dialogboxes;

import controller.GameController;
import datastructures.Phase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class PerformAnotherMoveVBox extends VBox implements RiskUIElement {

    public PerformAnotherMoveVBox() {
        applyStyling(this, "perform-another-move-vbox", "perform_another_move_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {

        HBox answers = new HBox();
        Text text = new Text("Do you want to perform another move operation?");

        answers.setAlignment(Pos.CENTER);
        answers.setPadding(new Insets(50,0,0,0));
        answers.setSpacing(30);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().setTurnManually(Phase.MOVE);

            }
        });

        Button noButton = new Button("No");
        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                GUIControl.getInstance().forwardTurnPhase();
            }
        });

        answers.getChildren().add(yesButton);
        answers.getChildren().add(noButton);



        this.getChildren().add(text);
        this.getChildren().add(answers);

    }
}
