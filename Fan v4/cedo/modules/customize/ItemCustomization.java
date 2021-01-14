package cedo.modules.customize;

import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class ItemCustomization extends Module {
    public NumberSetting handspeed = new NumberSetting("Hand Speed", 1, 1, 12, 1);
    public NumberSetting swordXValue = new NumberSetting("Sword X", 52, 1, 100, 1);
    public NumberSetting swordYValue = new NumberSetting("Sword Y", 56, 1, 100, 1);
    public NumberSetting itemSize = new NumberSetting("Item Size", 56, 1, 100, 1);

    public ItemCustomization() {
        super("Item Customize", Keyboard.KEY_NONE, Category.CUSTOMIZE);
        addSettings(handspeed, swordXValue, swordYValue, itemSize);
    }

    public void toggle() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
