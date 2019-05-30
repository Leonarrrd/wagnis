package view.gui.boxes;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class LogHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {
    public LogHBox (){
        super();
        addAsUpdateElement("LogHBox", this);
    }

    @Override
    public void doStuff() {
//        this.setPrefWidth(50);

        TextArea textArea = new TextArea("Welcome to Risk!");
        textArea.setMaxHeight(140);
        textArea.setPrefWidth(300);
        textArea.setEditable(false);
        textArea.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        textArea.setWrapText(true);

        textArea.appendText("\nThe current gamestate:");
        textArea.appendText(GUIControl.getInstance().getGame().toString());


        textArea.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));

        this.getChildren().add(textArea);
    }

    @Override
    public void update() {

    }
}
