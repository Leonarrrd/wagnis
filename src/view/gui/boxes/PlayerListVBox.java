package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

import java.util.ArrayList;
import java.util.List;

public class PlayerListVBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {

    public PlayerListVBox(){
        super();

        addAsUpdateElement("PlayerListVBox", this);
    }

    @Override
    public void doStuff() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        VBox playerListVBox = new VBox();
        playerListVBox.setAlignment(Pos.CENTER);

        List<Text> playerNames = new ArrayList<>();
        for (Player player : GUIControl.getInstance().getGame().getPlayers()){
            Text text = new Text(player.getName());
            text.setStyle("-fx-font: normal bold 20px 'serif' ");
            playerNames.add(text);

        }
        playerListVBox.getChildren().addAll(playerNames);

        VBox colorVBox = new VBox();
        colorVBox.setAlignment(Pos.CENTER);
        List<Text> colors = new ArrayList<>();
        for (Player player : GUIControl.getInstance().getGame().getPlayers()){
            Text text = new Text(player.getColor().name());
            text.setStyle("-fx-font: normal bold 20px 'serif' ");
            colors.add(text);
        }
        colorVBox.getChildren().addAll(colors);

        VBox turnVBox = new VBox();
        turnVBox.setAlignment(Pos.CENTER);
        List<Text> turn = new ArrayList<>();
        for (Player player : GUIControl.getInstance().getGame().getPlayers()){
            Text text = new Text("");
            if (GUIControl.getInstance().getGame().getTurn().getPlayer().equals(player)){
                text.setText(GUIControl.getInstance().getGame().getTurn().getPhase().name());
            }
            text.setStyle("-fx-font: normal bold 20px 'serif' ");
            turn.add(text);
        }
        turnVBox.getChildren().addAll(turn);

        this.getChildren().add(playerListVBox);
        this.getChildren().add(colorVBox);
        this.getChildren().add(turnVBox);
    }

    @Override

    public void update() {
//        VBox turnVBox = (VBox) this.getChildren().get(2);
//        for (int i = 0; i < turnVBox.getChildren().size() ; i++){
//            Node node = turnVBox.getChildren().get(i);
//            Text text = (Text) node;
//            text.setText("");
//            if (GUIControl.getInstance().getGame().getTurn().getPlayer().equals(player)){
//                text.setText(GUIControl.getInstance().getGame().getTurn().getPhase().name());
//            }
//        }
    }
}
