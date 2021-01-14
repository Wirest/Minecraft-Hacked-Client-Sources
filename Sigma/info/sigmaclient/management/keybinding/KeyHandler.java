package info.sigmaclient.management.keybinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Keyboard;

import info.sigmaclient.Client;

public class KeyHandler {
    private static final HashMap<Integer, ArrayList<Keybind>> registeredKeys = new HashMap<>();
    private static final List<KeyMask> activeMasks = new ArrayList<>();

    public static void update(boolean isInGui) {
        if (Client.isHidden()) {
            return;
        }
        boolean pressed = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        KeyHandler.updateMasks(key, pressed);
        if (isInGui) {
            // If the user is not in-game, skip.
            // return;
        }
        if (key == Keyboard.CHAR_NONE) {
            return;
        } else if (key == Keyboard.KEY_ESCAPE) {
            // If the key is escape the user most likely is trying to leave a
            // menu. This means its ok to clear the keybinds.
            activeMasks.clear();
        }
        if (!KeyHandler.keyHasBinds(key)) {
            // If the key has no registered keybinds, skip.
            return;
        }
        ArrayList<Keybind> set = new ArrayList<>();
        set.addAll(KeyHandler.registeredKeys.get(key));
        for (Keybind keybind : set) {
            // Press and unpress keybinds
            if (isMaskDown(keybind.getMask())) {
                if (pressed) {
                    keybind.press();
                } else {
                    keybind.release();
                }
            }
        }
    }

    /**
     * Updates the active masks in the keyHandler.
     */
    private static void updateMasks(int key, boolean pressed) {
        for (KeyMask mask : KeyMask.values()) {
            if (mask.equals(KeyMask.None)) {
                continue;
            }
            for (int keyInList : mask.getKeys()) {
                if (keyInList == key) {
                    boolean contains = KeyHandler.activeMasks.contains(mask);
                    if (pressed) {
                        if (!contains) {
                            KeyHandler.activeMasks.add(mask);
                        }
                    } else {
                        if (contains) {
                            KeyHandler.activeMasks.remove(mask);
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * Checks if a Keybind is registered.
     */
    public static boolean isRegistered(Keybind keybind) {
        int key = keybind.getKeyInt();
        boolean hasKeys = KeyHandler.keyHasBinds(key);
        return hasKeys && KeyHandler.registeredKeys.get(key).contains(keybind);
    }

    /**
     * Registers a keybind.
     */
    public static void register(Keybind keybind) {
        int key = keybind.getKeyInt();
        boolean hasKeys = KeyHandler.keyHasBinds(key);
        if (hasKeys) {
            if (!KeyHandler.registeredKeys.get(key).contains(keybind)) {
                KeyHandler.registeredKeys.get(key).add(keybind);
            }
        } else {
            ArrayList<Keybind> keyList = new ArrayList<>();
            keyList.add(keybind);
            KeyHandler.registeredKeys.put(key, keyList);
        }
    }

    /**
     * Updates a keybind with another's information.
     */
    public static void update(Bindable owner, Keybind keybind, Keybind newBind) {
        int key = keybind.getKeyInt();
        int newKey = newBind.getKeyInt();

        boolean hasKeys = KeyHandler.keyHasBinds(key);
        if (hasKeys) {
            for (Keybind regKey : KeyHandler.registeredKeys.get(key)) {
                if (regKey.getBindOwner().equals(owner)) {
                    regKey.update(newBind);
                    return;
                }
            }
        }
    }

    /**
     * Unregisters a keybind.
     */
    public static void unregister(Bindable owner, Keybind keybind) {
        int key = keybind.getKeyInt();
        boolean hasKeys = KeyHandler.keyHasBinds(key);
        if (hasKeys) {
            ArrayList<Keybind> list = KeyHandler.registeredKeys.get(key);
            int in = -1, i = 0;
            for (Keybind bind : list) {
                if (bind.getBindOwner().equals(owner)) {
                    in = i;
                }
                i++;
            }
            if (in >= 0) {
                list.remove(in);
            }
        }
    }

    /**
     * Checks if a key has keybinds registered to it.
     */
    public static boolean keyHasBinds(int key) {
        return KeyHandler.registeredKeys.containsKey(key);
    }

    /**
     * Checks if a given mask is currently held down.
     */
    static boolean isMaskDown(KeyMask mask) {
        return (mask == null || mask == KeyMask.None) || KeyHandler.activeMasks.contains(mask);
    }
}
