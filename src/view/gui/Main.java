package view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.gui.roots.StartBorderPane;
import view.gui.scenes.MainScene;

public class Main extends Application {



    @Override
    public void start(Stage stage) {
        Scene startupScene = new MainScene(new StartBorderPane());
        stage.setTitle("Risiko - Leonard T. & Philipp M.");
        stage.setScene(startupScene);
        stage.show();
    }
}
