package view.gui.helper;

public interface Updatable {

    /**
     * Adds this to an updatable Map with an Id.
     * Need to be called in the constructor of the Updatable-Element
     * @param id
     * @param updateable
     */
    default void addAsUpdateElement(String id, Updatable updateable) {
        GUIControl.getInstance().getComponentMap().put(id, updateable);
    }

    /**
     * Every GUI-Element that implements Updatable should know how to update itself.
     * If there is a change to the infomration that the Node displays this method will have to be called.
     */
    void update();
}
