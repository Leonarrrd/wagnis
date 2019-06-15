package view.gui.panes;

import datastructures.Color;
import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;
import view.gui.roots.GameBorderPane;
import view.gui.sockets.GameControllerFacade;
import view.gui.viewhelper.PlayerColorItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StartNewGameGridPane extends GridPane implements RiskUIElement, Updatable {

    VBox playerList = new VBox();

    public StartNewGameGridPane() {
        applyStyling(this, "start-new-game-grid-pane", "start_new_game_grid_pane.css");
        addAsUpdateElement(getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {


        UUID gameId = UUID.randomUUID();
        String hostPlayerName = "Hannes";
        GameControllerFacade.getInstance().createGameRoom(gameId, hostPlayerName, null);

        Text text = new Text(hostPlayerName);
        text.getStyleClass().add("player-name-text");

        playerList.getChildren().add(text);

        this.add(playerList, 0, 0);

        Button startGameButton = new Button("Start game");
        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    GameControllerFacade.getInstance().initNewGame(gameId);
                } catch (IOException | InvalidFormattedDataException | MaximumNumberOfPlayersReachedException | InvalidPlayerNameException | GameNotFoundException | CountriesAlreadyAssignedException | NoSuchPlayerException e) {
                    e.printStackTrace();
                }
            }
        });

        this.add(startGameButton, 1, playerList.getChildren().size());

    }

    @Override
    public void update() {
        playerList.getChildren().clear();
        for (String playerName : GUIControl.getInstance().getPlayersInLobby()) {
            playerList.getChildren().add(playerNameText(playerName));
        }
    }

    private Text playerNameText(String playerName) {

        Text text = new Text();
        text.getStyleClass().add("player-name-text");
        return text;

    }
}
