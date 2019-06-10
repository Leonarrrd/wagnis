package view.gui.boxes;

import datastructures.CardSymbol;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class CardsHBox extends BottomBarNodeHBox implements RiskUIElement, Updatable {
    private Text infantryCards;
    private Text cavalryCards;
    private Text artilleryCards;
    public CardsHBox() {
        super();
        addAsUpdateElement("cards-hbox", this);
//        applyStyling(this, "cards-hbox", "cards_hbox.css");
    }

    @Override
    public void doStuff() {
//        this.getStyleClass().add("cards-hbox");

        Player player = GUIControl.getInstance().getGame().getTurn().getPlayer();
        this.infantryCards = new Text("Infantry: " + player.getNumberOfCardsWithSymbol(CardSymbol.INFANTRY));
        this.cavalryCards = new Text("Cavalry: " + player.getNumberOfCardsWithSymbol(CardSymbol.CAVALRY));
        this.artilleryCards = new Text("Artillery: " + player.getNumberOfCardsWithSymbol(CardSymbol.ARTILLERY));
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(new Text("Your cards:"));
        vBox.getChildren().add(infantryCards);
        vBox.getChildren().add(cavalryCards);
        vBox.getChildren().add(artilleryCards);
        this.getChildren().add(vBox);
    }

    @Override
    public void update() {
        Player player = GUIControl.getInstance().getGame().getTurn().getPlayer();
        this.infantryCards.setText("Infantry: " + player.getNumberOfCardsWithSymbol(CardSymbol.INFANTRY));
        this.cavalryCards.setText("Cavalry: " + player.getNumberOfCardsWithSymbol(CardSymbol.CAVALRY));
        this.artilleryCards.setText("Artillery: " + player.getNumberOfCardsWithSymbol(CardSymbol.ARTILLERY));
    }

}
