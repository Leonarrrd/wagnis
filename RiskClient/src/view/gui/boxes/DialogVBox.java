package view.gui.boxes;

import exceptions.GameNotFoundException;
import javafx.scene.layout.VBox;
import view.gui.alerts.ErrorAlert;
import view.gui.boxes.dialogboxes.*;
import view.gui.helper.GUIControl;
import view.gui.helper.RiskUIElement;
import view.gui.helper.Updatable;

import java.io.IOException;

public class DialogVBox extends VBox implements RiskUIElement, Updatable {
    public DialogVBox() {
        applyStyling(this, "dialog-vbox", "dialog_vbox.css");
        addAsUpdateElement(getId(), this);
        init();
    }

    @Override
    public void init() {
        setLayoutY(640);
        setLayoutX(10);
        this.getChildren().add(new VBox());
        this.update();
    }

    @Override
    public void update() {
        try {
            if (GUIControl.getInstance().myTurn()) {
                switch (GUIControl.getInstance().getGame().getTurn().getPhase()) {
                    case USE_CARDS:
                        this.getChildren().set(0, new UseCardsVBox());
                        break;
                    case PLACE_UNITS:
                        this.getChildren().set(0, new PlaceUnitsVBox());
                        break;
                    case ATTACK:
                        this.getChildren().set(0, new AttackVBox());
                        break;
                    case DEFENSE:
                        this.getChildren().set(0, new WaitVBox());
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
            } else {
                this.getChildren().set(0, new WaitVBox());
            }
        } catch (GameNotFoundException | IOException | ClassNotFoundException e) {
            new ErrorAlert(e);
        }
    }
}
