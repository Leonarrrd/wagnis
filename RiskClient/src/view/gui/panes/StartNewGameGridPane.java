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
    private String hostPlayerName;


    /**
     * Constructor for player joining the game
     * also called in the initialization for the hostplayer
     */
    public StartNewGameGridPane() {

        applyStyling(this, "start-new-game-grid-pane", "start_new_game_grid_pane.css");
        addAsUpdateElement(getId(), this);
        this.add(playerList, 0, 0);
        doStuff();


    }

    /**
     * Constructor for initializing the game by the host player
     *
     * @param hostPlayerName Name for the host player provided by TextInputDialog beforehand
     */
    public StartNewGameGridPane(String hostPlayerName) {
        this();
        this.hostPlayerName = hostPlayerName;
        doStuff();
    }

    @Override
    public void doStuff() {

        if (this.hostPlayerName != null) {
            UUID gameId = UUID.randomUUID();

            try {
                GameControllerFacade.getInstance().createGameRoom(gameId, hostPlayerName, null);
            } catch (Exception e) {
                new ErrorAlert(e);
            }
            GUIControl.getInstance().getPlayersInLobby().add(hostPlayerName);
            Text text = new Text(hostPlayerName);
            text.getStyleClass().add("player-name-text");

            playerList.getChildren().add(text);


            Button startGameButton = new Button("Start game");
            startGameButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        GameControllerFacade.getInstance().initNewGame(gameId);

                    } catch (IOException | InvalidPlayerNameException | NoSuchPlayerException | GameNotFoundException | CountriesAlreadyAssignedException | InvalidFormattedDataException | MaximumNumberOfPlayersReachedException e) {
                        new ErrorAlert(e);
                    }
                }
            });

            this.add(startGameButton, 1, playerList.getChildren().size());

        }
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
