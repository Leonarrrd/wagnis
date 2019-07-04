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

public class LaunchAnotherAttackVBox extends VBox implements RiskUIElement {

    public LaunchAnotherAttackVBox() {
        applyStyling(this, "launch-another-attack-vbox", "launch_another_attack_vbox.css");
        init();
    }

    @Override
    public void init() {
        Text questionText = new Text("Do you want to launch another attack?");

        HBox answers = new HBox();
        answers.setAlignment(Pos.CENTER);
        answers.setPadding(new Insets(50,0,0,0));
        answers.setSpacing(30);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    GUIControl.getInstance().setTurnManually(Phase.ATTACK);
                } catch (NoSuchPlayerException | GameNotFoundException | IOException | NoSuchCardException | CardAlreadyOwnedException e) {
                    new ErrorAlert(e);
                }
            }
        });
        answers.getChildren().add(yesButton);

        Button noButton = new Button("No");
        answers.getChildren().add(noButton);

        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().forwardTurnPhase();
            }
        });

        this.getChildren().add(questionText);
        this.getChildren().add(answers);

    }
}
