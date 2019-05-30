package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class MissionHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {
    private Text missionText;

    public MissionHBox() {
        super();
        addAsUpdateElement("MissionHBox", this);
    }

    @Override
    public void doStuff() {
        this.setMaxWidth(300); // ????


        this.missionText= new Text(GUIControl.getInstance().getGame().getTurn().getPlayer().getMission().getMessage());
        missionText.setWrappingWidth(200);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(new Text("Your mission:"));
        vBox.getChildren().add(missionText);

        this.getChildren().add(vBox);
    }

    @Override
    public void update(){
        missionText.setText(GUIControl.getInstance().getGame().getTurn().getPlayer().getMission().getMessage());
    }
}
