package view.gui.boxes;

import javafx.scene.layout.VBox;
import view.gui.boxes.dialogboxes.*;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

public class DialogVBox extends VBox implements RiskUIElement, Updatable {
    public DialogVBox() {
        applyStyling(this, "dialog-vbox", "dialog_vbox.css");
        addAsUpdateElement(getId(), this);
        doStuff();
    }

    @Override
    public void doStuff() {
        this.getChildren().add(new VBox());
        this.update();
    }

    @Override
    public void update() {
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
