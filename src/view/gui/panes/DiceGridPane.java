package view.gui.panes;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Player;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

import java.util.List;

public class DiceGridPane extends GridPane implements RiskUIElement, Updatable {

    private boolean visible;
    public DiceGridPane() {
        applyStyling(this, "dice-grid", "dice_grid.css");
        addAsUpdateElement(this.getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {


    }

    @Override
    public void update() {

    }

    public void update(String attackingCountryPlayerColor, String defendingColorPlayerColor, List<Integer> attackerDices, List<Integer> defenderDices) {

        this.getChildren().clear();

        for(int i = 0; i<attackerDices.size(); i++) {
            this.add(fittedImageView(getImagePath(attackingCountryPlayerColor, attackerDices.get(i))), i, 0);
        }
        for(int i = 0; i<defenderDices.size(); i++) {
            this.add(fittedImageView(getImagePath(defendingColorPlayerColor, defenderDices.get(i))), i, 1);
        }
    }

    private String getImagePath(String color, int index) {
        return "file:assets/img/dices/"+ color + index+".png";
    }

    private ImageView fittedImageView(String url) {
        ImageView imgView = new ImageView(url);

        imgView.setFitWidth(64);
        imgView.setFitHeight(64);

        return imgView;
    }

}
