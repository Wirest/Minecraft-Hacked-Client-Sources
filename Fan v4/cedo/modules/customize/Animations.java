package cedo.modules.customize;

import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {
    public ModeSetting mode = new ModeSetting("Block", "1.8", "Whack", "Item360", "Swirl", "Penis", "1.8", "1.7", "Chill", "Tiny Whack", "Long Hit", "Butter", "Astolfo");

    public Animations() {
        super("Animations", Keyboard.KEY_O, Category.CUSTOMIZE);
        addSettings(mode);
    }

    public void toggle() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
