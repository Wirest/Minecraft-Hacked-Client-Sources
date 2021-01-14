package info.sigmaclient.management.keybinding;

import org.lwjgl.input.Keyboard;

public enum KeyMask {
    None(null), Shift(new int[]{Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT}), Control(new int[]{Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL}), Alt(new int[]{Keyboard.KEY_LMENU, Keyboard.KEY_RMENU});
    private final int[] keys;

    private KeyMask(int[] keys) {
        this.keys = keys;
    }

    public int[] getKeys() {
        return keys;
    }

    public static KeyMask getMask(String name) {
        for (KeyMask mask : values()) {
            if (mask.name().toLowerCase().startsWith(name.toLowerCase())) {
                return mask;
            }
        }
        return KeyMask.None;
    }
}
