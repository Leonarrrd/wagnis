package view.gui.panes;

import datastructures.Color;
import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.roots.GameBorderPane;
import view.gui.sockets.GameControllerFacade;
import view.gui.viewhelper.PlayerColorItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StartNewGameGridPane extends GridPane implements RiskUIElement {

    private List<PlayerColorItem> playerColorItems = new ArrayList();

    public StartNewGameGridPane() {
        applyStyling(this, "start-new-game-grid-pane", "start_new_game_grid_pane.css");
        doStuff();
    }

    @Override
    public void doStuff() {


        UUID gameId = UUID.randomUUID();

        GameControllerFacade.getInstance().createGameRoom(gameId,"Hannes");

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

        this.add(startGameButton, 0,0);

    }

}
