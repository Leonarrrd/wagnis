package view.gui.boxes;

import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import model.Player;
import view.gui.boxes.dialogboxes.*;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;
import view.gui.roots.StartBorderPane;

public class DialogVBox extends VBox implements RiskUIElement, Updatable {
    public DialogVBox() {
        applyStyling(this, "dialog-vbox", "dialog_vbox.css");
        addAsUpdateElement(getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {
        setLayoutY(640);
        setLayoutX(10);
        this.getChildren().add(new VBox());
        this.update();
    }

    @Override
    public void update() {
//        Player winner = GUIControl.getInstance().checkForWinner();
//        if (winner != null){
//            new Alert(Alert.AlertType.INFORMATION, winner.getName()+ " has won the Game!").showAndWait();
//            getScene().setRoot(new StartBorderPane());
//        }

        switch(GUIControl.getInstance().getGame().getTurn().getPhase()) {
            case USE_CARDS:
                this.getChildren().set(0, new UseCardsVBox());
                break;
            case PLACE_UNITS:
                this.getChildren().set(0, new PlaceUnitsVBox());
                break;
            case ATTACK:
                this.getChildren().set(0, new AttackVBox());
                break;
            case TRAIL_UNITS:
                this.getChildren().set(0, new TrailUnitsVBox());
                break;
            case PERFORM_ANOTHER_ATTACK:
                this.getChildren().set(0, new LaunchAnotherAttackVBox());
                break;
            case MOVE:
                this.getChildren().set(0, new MoveVBox());
                break;
            case PERFORM_ANOTHER_MOVE:
                this.getChildren().set(0, new PerformAnotherMoveVBox());
                break;
        }
    }
}
