package view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.gui.alerts.ErrorAlert;
import view.gui.panes.StartBorderPane;
import view.gui.scenes.StartScene;

import java.io.FileNotFoundException;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Scene startupScene = new StartScene(new StartBorderPane());
        stage.setScene(startupScene);
        stage.show();

    }


}
