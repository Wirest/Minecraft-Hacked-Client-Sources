package com.ihl.client.input;

import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class InputUtil {

    public static int[] mouse = new int[2];

    public static void tick() {
        mouse[0] = Mouse.getX();
        mouse[1] = Display.getHeight() - Mouse.getY();
    }

    public static void keyPress(int code, char keyChar) {
        if (code != Keyboard.KEY_NONE) {
            for (String key : Module.modules.keySet()) {
                Module module = Module.modules.get(key);
                if (Keyboard.getKeyName(code).equalsIgnoreCase(Option.get(module.options, "keybind").STRING())) {
                    module.toggle();
                }
            }
        }
    }

    public static void keyRelease(int code, char keyChar) {

    }

}
