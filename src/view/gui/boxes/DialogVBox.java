package view.gui.boxes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.RiskUIElement;

public class DialogVBox extends VBox implements RiskUIElement {
    public DialogVBox() {
        applyStyling(this, "dialog-vbox", "dialog_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        this.setPrefHeight(180);
        this.setMaxWidth(320);
        this.setMaxHeight(200);

        this.setAlignment(Pos.CENTER);

        Text question = new Text("Do you want to nachrueck units?");
        question.setStyle("-fx-font: 20 arial;");
        this.getChildren().add(question);

        HBox answers = new HBox();
        answers.setAlignment(Pos.CENTER);
        answers.setPadding(new Insets(50,0,0,0));
        answers.setSpacing(30);
        answers.getChildren().add(new Button("Yes"));
        answers.getChildren().add(new Button("No"));

        this.getChildren().add(answers);

    }
}
