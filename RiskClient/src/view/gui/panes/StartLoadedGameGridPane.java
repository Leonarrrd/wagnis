package view.gui.panes;

import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;
import view.gui.roots.GameBorderPane;
import view.gui.sockets.GameControllerFacade;
import view.gui.sockets.GameLobbyManager;
import view.gui.viewhelper.PlayerColorItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StartLoadedGameGridPane extends GridPane implements RiskUIElement, Updatable {

    VBox playerList = new VBox();
    private String hostPlayerName;
    private UUID gameId;

    /**
     * Constructor for player joining the game
     * also called in the initialization for the hostplayer
     */
    public StartLoadedGameGridPane(UUID gameId) {

        applyStyling(this, "start-new-game-grid-pane", "start_new_game_grid_pane.css");
        addAsUpdateElement(getId(), this);
        this.add(playerList, 0, 0);
        this.gameId = gameId;
        doStuff();
    }

    @Override
    public void doStuff() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Game game = GUIControl.getInstance().getGame();

        List<String> choices = new ArrayList<>();
        for (Player player : game.getPlayers()){
            choices.add(player.getName());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Player select");
        dialog.setHeaderText("Player select");
        dialog.setContentText("Select your name");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            this.hostPlayerName = result.get();
        }

    try {
        GameControllerFacade.getInstance().createGameRoom(gameId, hostPlayerName, null);
    } catch (Exception e) {
        new ErrorAlert(e);
    }

    GUIControl.getInstance().getPlayersInLobby().add(hostPlayerName);
    Text text = new Text(hostPlayerName);
    text.getStyleClass().add("player-name-text");

    playerList.getChildren().add(text);

    Button loadGameButton = new Button("Start game");
    loadGameButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (playerList.getChildren().size() == game.getPlayers().size()) {
//                correctPlayerNames();
                try {
                    GameLobbyManager.getInstance().startLoadedGame(gameId);
                } catch (ClassNotFoundException | InvalidFormattedDataException | GameNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Incorrect amount of players").showAndWait();
            }
        }
    });

    this.add(loadGameButton, 1, playerList.getChildren().size());

    TextField t = new TextField(gameId.toString());
    this.add(t, 0, playerList.getChildren().size() + 1);

    }


    @Override
    public void update() {
        playerList.getChildren().clear();
        for (String playerName : GUIControl.getInstance().getPlayersInLobby()) {
            playerList.getChildren().add(playerNameText(playerName));
        }
    }

    private Text playerNameText(String playerName) {
        Text text = new Text(playerName);
        text.getStyleClass().add("player-name-text");
        return text;

    }
}
