package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

import java.util.ArrayList;
import java.util.List;

public class PlayerListVBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {

    public PlayerListVBox() {
        super();
        addAsUpdateElement("player-list-vbox", this);
    }

    @Override
    public void doStuff() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        update();
    }

    @Override
    public void update() {
        this.getChildren().clear();

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        List<Text> playerNames = new ArrayList<>();
        for (Player player : GUIControl.getInstance().getGame().getPlayers()) {
            Text text = new Text(player.getName());
            text.setStyle("-fx-font: normal bold 20px 'serif' ");
            playerNames.add(text);
        }
        vBox.getChildren().addAll(playerNames);

        VBox colorVBox = new VBox();
        colorVBox.setAlignment(Pos.CENTER);
        List<Text> colors = new ArrayList<>();
        for (Player player : GUIControl.getInstance().getGame().getPlayers()) {
            Text text = new Text(player.getColor().name());
            text.setStyle("-fx-font: normal bold 20px 'serif' ");
            colors.add(text);
        }
        colorVBox.getChildren().addAll(colors);

        VBox turnVBox = new VBox();
        turnVBox.setAlignment(Pos.CENTER);
        List<Text> turn = new ArrayList<>();
        for (Player player : GUIControl.getInstance().getGame().getPlayers()) {
            Text text = new Text("");
            if (GUIControl.getInstance().getGame().getTurn().getPlayer().equals(player)) {
                text.setText(GUIControl.getInstance().getGame().getTurn().getPhase().name());
            }
            text.setStyle("-fx-font: normal bold 20px 'serif' ");
            turn.add(text);
        }
        turnVBox.getChildren().addAll(turn);

        this.getChildren().add(vBox);
        this.getChildren().add(colorVBox);
        this.getChildren().add(turnVBox);
    }
}
