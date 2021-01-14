package cedo.modules.customize;

import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {
    public ModeSetting mode = new ModeSetting("Block", "1.8", "Tap", "Whack", "Item360", "Swirl", "UpDown", "Penis", "1.8");
    public NumberSetting handspeed = new NumberSetting("Hand Speed", 1, 1, 12, 1);
    public NumberSetting swordXValue = new NumberSetting("Sword X", 52, 1, 100, 1);
    public NumberSetting swordYValue = new NumberSetting("Sword Y", 56, 1, 100, 1);

    public Animations() {
        super("Animations", Keyboard.KEY_O, Category.CUSTOMIZE);
        addSettings(mode, handspeed, swordXValue, swordYValue);
    }

    public void toggle() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
