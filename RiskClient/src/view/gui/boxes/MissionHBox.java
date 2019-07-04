package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;

public class MissionHBox extends BottomBarNodeHBox implements RiskUIElement {
    private Text missionText;

    public MissionHBox() {
        super();
    }

    @Override
    public void init() {
        for (Player player : GUIControl.getInstance().getGame().getPlayers()){
            if (player.getName().equals(GUIControl.getInstance().getPlayerName()))
                this.missionText = new Text(player.getMission().getMessage());
        }
        missionText.setWrappingWidth(200);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(new Text("Your mission:"));
        vBox.getChildren().add(missionText);

        this.getChildren().add(vBox);
    }
}
