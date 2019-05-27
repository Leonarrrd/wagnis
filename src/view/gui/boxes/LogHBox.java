package view.gui.boxes;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import view.gui.helper.RiskUIElement;

public class LogHBox extends BottomBarNodeHBox implements RiskUIElement {
    public LogHBox (){
        super();
    }

    @Override
    public void doStuff() {
        TextArea textArea = new TextArea("I'm a textArea");
        textArea.setMaxHeight(140);
        textArea.setPrefWidth(300);
        textArea.setEditable(false);
        textArea.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        textArea.appendText("\nPhillip riecht nach Tümpel");
        textArea.appendText("\nNorway attacks Finland");
        textArea.appendText("\nFinland loses 2 units");
        textArea.appendText("\nNorway loses 1 unit");
        textArea.appendText("\nPhillip ends his turn");
        textArea.appendText("\nPhillip ends his turn");
        textArea.appendText("\nPhillip ends his turn");
        textArea.appendText("\nPhillip ends his turn");
        textArea.appendText("\nPhillip ends his turn");


        textArea.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));

        this.getChildren().add(textArea);
    }
}
