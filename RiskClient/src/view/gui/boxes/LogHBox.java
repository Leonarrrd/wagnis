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

/**
 * TODO: Work in progress - updates not yet done
 */
public class LogHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {

    TextArea textArea;

    public LogHBox (){
        super();
        addAsUpdateElement("log-hbox", this);
    }

    @Override
    public void init() {

        textArea = new TextArea("Welcome to Risk!");
        textArea.setMaxHeight(140);
        textArea.setPrefWidth(300);
        textArea.setMinWidth(400);
        textArea.setEditable(false);
        textArea.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        textArea.setWrapText(true);

        textArea.appendText("\nThe current gamestate:");
        textArea.appendText(GUIControl.getInstance().getGame().toString());
        textArea.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));

        this.getChildren().add(textArea);
    }

    @Override
    public void update() {
    }

    public void update(String text) {
        textArea.appendText(text);
        textArea.appendText("\n");
    }

}
