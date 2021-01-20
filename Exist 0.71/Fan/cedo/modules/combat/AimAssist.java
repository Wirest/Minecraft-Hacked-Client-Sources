package cedo.modules.combat;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class AimAssist extends Module {
    public NumberSetting senschange = new NumberSetting("Sens Change", 0.2, 0.1, 1, 0.1);
    public float oldsens;

    public AimAssist() {

        super("AimAssist", Keyboard.KEY_NONE, Category.COMBAT);
        addSettings(senschange);
    }


    public void onEnable() {
        oldsens = mc.gameSettings.mouseSensitivity;
        super.onEnable();
    }

    public void onEvent(Event e) {
        this.setSuffix(String.valueOf(senschange.getValue()));
        if (e instanceof EventMotion) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null)
                mc.gameSettings.mouseSensitivity = 0.1f;
            else
                mc.gameSettings.mouseSensitivity = oldsens;

        }


    }
}