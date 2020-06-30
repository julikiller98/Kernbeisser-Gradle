package kernbeisser.CustomComponents;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeyCapture {

    HashMap<Integer,Runnable> keyFunctions = new HashMap<Integer,Runnable>();

    public boolean processKeyEvent(KeyEvent e) {
        boolean isMappedKey = keyFunctions.containsKey(e.getKeyCode());
        if (isMappedKey && e.getID() == KeyEvent.KEY_RELEASED){
            keyFunctions.get(e.getKeyCode()).run();
        }
        return isMappedKey;
    }

    public void add(int key, Runnable cmd) {
        keyFunctions.put(key, cmd);
    }

    public void remove (int key) {
        keyFunctions.remove(key);
    }

    public void clear() {
        keyFunctions.clear();
    }
}