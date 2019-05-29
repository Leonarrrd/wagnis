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
//        Scene startupScene = new MainScene(new GameBorderPane());
        stage.setTitle("Risiko - Leonard der Gestaltige & Big Assistent");
        stage.setScene(startupScene);
        stage.show();
    }
}
