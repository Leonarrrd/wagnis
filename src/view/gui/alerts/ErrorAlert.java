package view.gui.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import view.gui.helper.RiskUIElement;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorAlert extends Alert implements RiskUIElement {
    private String titleText;
    private String headerText;
    private String contentText;
    private Exception exception;

    public ErrorAlert(String titleText, String headerText, String contentText) {
        super(AlertType.ERROR);
        this.titleText = titleText;
        this.headerText = headerText;
        this.contentText = contentText;
        doStuff();
    }

    public ErrorAlert(Exception exception) {
        super(AlertType.ERROR);
        this.exception = exception;
        doStuffException();
    }


    @Override
    public void doStuff() {
        this.setTitle(this.titleText);
        this.setHeaderText(this.headerText);
        if (this.exception == null) {
            this.setContentText(this.contentText);
            this.showAndWait();
        }

    }

    private void doStuffException() {

        if (this.exception != null) {
            this.setTitle(this.exception.getMessage().toString());
            this.setHeaderText("Stacktrace: ");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            this.exception.printStackTrace(pw);
            String exText = sw.toString();

            Label label = new Label("Following exception stack trace has been printed: ");
            TextArea textArea = new TextArea(exText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            this.getDialogPane().setExpandableContent(expContent);
            this.showAndWait();
        }

    }
}
