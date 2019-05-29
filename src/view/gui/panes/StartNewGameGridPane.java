package view.gui.panes;

import controller.GameController;
import datastructures.Color;
import exceptions.CountriesAlreadyAssignedException;
import exceptions.GameNotFoundException;
import exceptions.InvalidFormattedDataException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Game;
import model.Player;
import view.gui.alerts.ErrorAlert;
import view.gui.helper.RiskUIElement;
import view.gui.roots.GameBorderPane;
import view.gui.viewhelper.PlayerColorItem;

import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartNewGameGridPane extends GridPane implements RiskUIElement {

    private List<PlayerColorItem> playerColorItems = new ArrayList();

    public StartNewGameGridPane() {
        applyStyling(this, "start-new-game-grid-pane", "start_new_game_grid_pane.css");
        doStuff();
    }

    @Override
    public void doStuff() {

        playerColorItems.add(new PlayerColorItem(playerTextField(), colorComboBox()));
        playerColorItems.add(new PlayerColorItem(playerTextField(), colorComboBox()));
        fillGridWithListItems();
        this.add(addAnotherPlayerButton(), 0, playerColorItems.size());
        this.add(startGameButton(), 2, playerColorItems.size()+2);
    }


    private void updatePlayerFields() {
        this.getChildren().clear();
        fillGridWithListItems();
        if (playerColorItems.size() < 5) {
            this.add(addAnotherPlayerButton(), 0, playerColorItems.size());
        }
        if(playerColorItems.size() > 2) {
            this.add(removePlayerButton(), 2, playerColorItems.size()-1);
        }
        this.add(startGameButton(), 2, playerColorItems.size()+2);

    }

    private ComboBox colorComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        List<String> enumValues = new ArrayList<>();
        for (Object o : Color.class.getEnumConstants()) {
            enumValues.add(o.toString());
        }

        ObservableList<String> colors = FXCollections.observableArrayList(enumValues);
        comboBox.setItems(colors);

        return comboBox;
    }

    private TextField playerTextField() {
        TextField textField = new TextField();

        return textField;
    }

    private Button addAnotherPlayerButton() {
        Button addAnotherPlayerButton = new Button("+");
        addAnotherPlayerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerColorItems.add(new PlayerColorItem(playerTextField(), colorComboBox()));
                updatePlayerFields();
            }
        });
        return addAnotherPlayerButton;
    }

    private Button removePlayerButton() {
        Button removePlayerButton = new Button("-");
        removePlayerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerColorItems.remove(playerColorItems.size()-1);
                updatePlayerFields();
            }
        });
        return removePlayerButton;
    }

    private Button startGameButton() {
        Button startGameButton = new Button("Start game");
        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame();
            }
        });
        return startGameButton;
    }

    private void fillGridWithListItems() {
        for (int i = 0; i < playerColorItems.size(); i++) {
            this.add(playerColorItems.get(i).getPlayerTextField(), 0, i);
            this.add(playerColorItems.get(i).getPlayerComboBox(), 1, i);
        }
    }


    private void startGame() {
        //MARK: access to game logic
        Game game = null;
        try {
            game = GameController.getInstance().initNewGame();
        } catch (IOException | InvalidFormattedDataException e) {
            new ErrorAlert(Alert.AlertType.ERROR, e);
        }
        List<Player> players = new ArrayList<>();
        for(PlayerColorItem pci : playerColorItems) {
            if(pci.getPlayerTextField().getText().equals("") || pci.getPlayerTextField().getText() == null) {
                new ErrorAlert(Alert.AlertType.INFORMATION, "Game could not be started.", "Please select a name.", "Please make sure every Player has a name selected.");
                return;
            }
            String playerName = pci.getPlayerTextField().getText();

            if(pci.getPlayerComboBox().getSelectionModel().getSelectedItem() == null) {
                new ErrorAlert(Alert.AlertType.INFORMATION, "Game could not be started.", "Please select a color.", "Please make sure every Player has a different color selected.");
                return;
            }
            Color playerColor = Color.valueOf((String) pci.getPlayerComboBox().getSelectionModel().getSelectedItem());
            Player p = new Player(playerName, playerColor);
            players.add(p);
        }

//        // should not happen here?
//        game.setPlayers(players);
//        try {
//            GameController.getInstance().setTurn(game.getId());
//            GameController.getInstance().assignMissions(game.getId());
//            GameController.getInstance().assignCountries(game.getId());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(game);
        getScene().setRoot(new GameBorderPane());
    }
}
