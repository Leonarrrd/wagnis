package view.gui.boxes.dialogboxes;

import datastructures.Phase;
import exceptions.GameNotFoundException;
import exceptions.NoSuchPlayerException;
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
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;

import java.io.IOException;

public class TrailUnitsVBox extends VBox implements RiskUIElement {

    Spinner<Integer> trailUnitsSpinner;

    public TrailUnitsVBox() {
        applyStyling(this, "trail-units-vbox", "trail_units_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        HBox answers = new HBox();
        Text text = new Text("Do you want to trail units?");

        answers.setAlignment(Pos.CENTER);
        answers.setPadding(new Insets(50,0,0,0));
        answers.setSpacing(30);


        Button trailUnitsButton = new Button("Trail Units!");

        int maxValue = GUIControl.getInstance().getLastFightCountry().getSrcCountry().getUnits()-1;
        trailUnitsSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxValue, 0);
        trailUnitsSpinner.setValueFactory(valueFactory);
        trailUnitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    GUIControl.getInstance().trailUnits(trailUnitsSpinner.getValue());
                    GUIControl.getInstance().setTurnManually(Phase.ATTACK);
                } catch (NoSuchPlayerException | GameNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button skipButton = new Button("No");
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUIControl.getInstance().forwardTurnPhase();
            }
        });

        answers.getChildren().add(trailUnitsButton);
        answers.getChildren().add(skipButton);

        this.getChildren().add(text);
        this.getChildren().add(trailUnitsSpinner);
        this.getChildren().add(answers);
    }
}
