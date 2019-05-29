package view.gui.helper;

public interface Updatable {

    default void addAsUpdateElement(String id, Updatable updateable) {
        GUIControl.getInstance().componentMap.put(id, updateable);
    }

    void update();
}
