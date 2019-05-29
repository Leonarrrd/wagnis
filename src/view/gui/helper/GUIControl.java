package view.gui.helper;

import java.util.HashMap;
import java.util.Map;

public class GUIControl {

    private static GUIControl instance;
    public Map<String, Updateable> componentMap = new HashMap<>();

    private GUIControl() {}

    public static GUIControl getInstance() {
        if(instance == null) {
            instance = new GUIControl();
        }
        return instance;
    }

    public void update() {

    }
}
