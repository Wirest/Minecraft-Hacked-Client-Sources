package cedo.modules.render;

import cedo.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoBob extends Module {

    public NoBob() {
        super("NoBob", Keyboard.KEY_O, Category.RENDER);
    }

    public void onEnable() {
        mc.gameSettings.viewBobbing = false;
        super.onEnable();
    }

    public void onDisable() {
        mc.gameSettings.viewBobbing = true;
        super.onDisable();
    }
}

