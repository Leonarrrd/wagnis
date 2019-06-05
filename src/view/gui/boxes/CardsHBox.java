package view.gui.boxes;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Card;
import model.Player;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class CardsHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {
    private Text oneStarCards;
    private Text twoStarCards;
    public CardsHBox() {
        super();
        addAsUpdateElement("cards-hbox", this);
//        applyStyling(this, "cards-hbox", "cards_hbox.css");
    }

    @Override
    public void doStuff() {
//        this.getStyleClass().add("cards-hbox");

        Player player = GUIControl.getInstance().getGame().getTurn().getPlayer();
        this.oneStarCards = new Text("One Star Cards: " + player.getNumberOfCardsWithXStars(1));
        this.twoStarCards = new Text("Two Star Cards: " + player.getNumberOfCardsWithXStars(2));
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(new Text("Your cards:"));
        vBox.getChildren().add(oneStarCards);
        vBox.getChildren().add(twoStarCards);
        this.getChildren().add(vBox);
    }

    @Override
    public void update() {
        Player player = GUIControl.getInstance().getGame().getTurn().getPlayer();
        this.oneStarCards.setText("One Star Cards: " + player.getNumberOfCardsWithXStars(1));
        this.twoStarCards.setText("Two Star Cards: " + player.getNumberOfCardsWithXStars(2));
    }

}
