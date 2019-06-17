package view.gui.boxes;

import exceptions.GameNotFoundException;
import exceptions.InvalidPlayerNameException;
import exceptions.MaximumNumberOfPlayersReachedException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.RiskUIElement;
import view.gui.panes.StartNewGameGridPane;
import view.gui.roots.StartBorderPane;
import view.gui.sockets.GameControllerFacade;

import java.io.IOException;
import java.util.UUID;

public class JoinGameVBox extends VBox implements RiskUIElement {

    public JoinGameVBox() {
        applyStyling(this, "join-game-vbox", "join_game_vbox.css");
        doStuff();
    }

    @Override
    public void doStuff() {
        TextField textFieldId = new TextField("Insert UUID");
        getChildren().add(textFieldId);

        TextField playerName = new TextField("Name");
        getChildren().add(playerName);

        Button joinGame = new Button("Join Game");

        joinGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ((StartBorderPane) getParent()).setCenter(new StartNewGameGridPane());
                    GameControllerFacade.getInstance().addPlayer(UUID.fromString(textFieldId.getText()), playerName.getText(), null);
                } catch (IOException | GameNotFoundException | MaximumNumberOfPlayersReachedException | InvalidPlayerNameException e) {
                    new ErrorAlert(e);
                }
            }
        });
        getChildren().add(joinGame);


    }
}
