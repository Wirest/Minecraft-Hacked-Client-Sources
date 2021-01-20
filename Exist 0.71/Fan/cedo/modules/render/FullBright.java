package cedo.modules.render;

import cedo.events.Event;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class FullBright extends Module {
    public NumberSetting brightness = new NumberSetting("Brightness", 10, 1, 10, 1);

    public FullBright() {
        super("FullBright", Keyboard.KEY_O, Category.RENDER);
        addSettings(brightness);
    }

    public void onEvent(Event e) {
        mc.gameSettings.gammaSetting = (float) brightness.getValue();
    }

    public void onDisable() {
        mc.gameSettings.gammaSetting = 1;
        super.onDisable();
    }
}
