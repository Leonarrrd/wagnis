package view.gui.helper;

public interface Updatable {

    default void addAsUpdateElement(String id, Updatable updateable) {
        GUIControl.getInstance().getComponentMap().put(id, updateable);
    }

    void update();
}
