package cheatware.module.render;

import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;

public class Fullbright extends Module {
    private float oldBrightness;

    public Fullbright() {
        super("Fullbright", Keyboard.KEY_C, Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        oldBrightness = mc.gameSettings.gammaSetting;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.gameSettings.gammaSetting = 10F;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.gameSettings.gammaSetting = oldBrightness;
    }
}
