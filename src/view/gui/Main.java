package view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.gui.panes.StartBorderPane;
import view.gui.scenes.StartScene;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Scene startupScene = new StartScene(new StartBorderPane());
        stage.setTitle("Risiko - Leonard T. & Philipp M.");

        stage.setScene(startupScene);
        stage.show();

    }


}
