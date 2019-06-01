package view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.gui.roots.GameBorderPane;
import view.gui.roots.StartBorderPane;
import view.gui.scenes.MainScene;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {


    @Override
    public void start(Stage stage) {
        Scene startupScene = new MainScene(new StartBorderPane());
        stage.setTitle("Risiko - Leonard der Gestaltige & Big Assistent");
        stage.setScene(startupScene);
        stage.setResizable(false);
        stage.show(); }


}
