package view.gui.helper;

public interface Updateable {

    default void addAsUpdateElement(String id, Updateable updateable) {
        GUIControl.getInstance().componentMap.put(id, updateable);
    }

    void update();
}
