package view.gui.boxes;

import exceptions.GameNotFoundException;
import exceptions.InvalidPlayerNameException;
import exceptions.MaximumNumberOfPlayersReachedException;
import helper.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;
import view.gui.panes.StartNewGameGridPane;
import view.gui.roots.StartBorderPane;
import view.gui.sockets.GameControllerFacade;
import view.gui.sockets.GameLobbyManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JoinGameVBox extends VBox implements RiskUIElement, Updatable {

    public JoinGameVBox() {
        applyStyling(this, "join-game-vbox", "join_game_vbox.css");
        addAsUpdateElement(this.getId(), this);
        init();
    }

    @Override
    public void init() {
        TextField textFieldId = new TextField("Insert UUID");
        getChildren().add(textFieldId);
        Button joinGame = new Button("Join Game");

        joinGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (textFieldId.getText().matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                    try {
                        GameLobbyManager.getInstance().checkGameType(textFieldId.getText());
                    } catch (IOException e) {
                        new ErrorAlert(e);
                    }
                    } else {
                    new Alert(Alert.AlertType.INFORMATION, "Not a UUID").showAndWait();
                }

            }
        });
        getChildren().add(joinGame);
    }

    public void handleGameType(String gameId, String gameType, List<String> playerNames){
        switch (gameType){
            case "newGame":
                joinNewGame(gameId);
                break;
            case "loadedGame":
                joinLoadedGame(gameId, playerNames);
                break;
            case "invalid":
                new Alert(Alert.AlertType.INFORMATION, "No Lobby for game with given ID").showAndWait();
                break;
        }
    }

    private void joinNewGame(String gameId){
        getChildren().remove(0, getChildren().size());

        TextField playerNameTextField = new TextField("Name");
        Button joinGame = new Button("Join Game");
        joinGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = playerNameTextField.getText();
                if (!Utils.stringContainsDelimitters(name) && name.length() < 10) {
                    try {
                        ((StartBorderPane) getParent()).setCenter(new StartNewGameGridPane());
                        GameControllerFacade.getInstance().addPlayer(UUID.fromString(gameId), name, null);
                    } catch (IOException | GameNotFoundException | InvalidPlayerNameException | MaximumNumberOfPlayersReachedException e) {
                        new ErrorAlert(e);
                    }
                } else {
                    new ErrorAlert("Invalid Player Name", "Please choose another name",
                            "Please do not use , ' - : in your name. Also, your name must not be longer than 10 characters");
                }
            }
        });
        getChildren().add(playerNameTextField);
        getChildren().add(joinGame);
    }

    private void joinLoadedGame(String gameId, List<String> playerNames){
        getChildren().remove(0, getChildren().size());
        ChoiceDialog<String> dialog = new ChoiceDialog<>(playerNames.get(0), playerNames);
        dialog.setTitle("Player select");
        dialog.setHeaderText("Player select");
        dialog.setContentText("Select your name");

        Optional<String> result = dialog.showAndWait();

        String playerName = "";
        if (result.isPresent()){
            playerName = result.get();
        }
        try {
            ((StartBorderPane) getParent()).setCenter(new StartNewGameGridPane());
            GameControllerFacade.getInstance().addPlayer(UUID.fromString(gameId), playerName, null);
        } catch (IOException | GameNotFoundException | InvalidPlayerNameException | MaximumNumberOfPlayersReachedException e) {
            new ErrorAlert(e);
        }
    }

    /**
     * FIXME: Empty update method so that it is accessible as via component map from the IOThread
     *  potential better solution: scene.lookup()-method
     */
    @Override
    public void update() {
    }
}
