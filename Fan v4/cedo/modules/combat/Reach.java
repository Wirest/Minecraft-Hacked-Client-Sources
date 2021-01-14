package cedo.modules.combat;

import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {

    public NumberSetting reach = new NumberSetting("Reach", 3, 1, 6, 0.1);

    public Reach() {
        super("Reach", Keyboard.KEY_NONE, Category.COMBAT);
        addSetting(reach);
    }
}
