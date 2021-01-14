package cedo.settings.impl;

import cedo.settings.Setting;
import org.lwjgl.input.Keyboard;

public class KeybindSetting extends Setting {

    private int code;

    public KeybindSetting(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code == -1 ? Keyboard.KEY_NONE : code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
