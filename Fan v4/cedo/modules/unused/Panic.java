package cedo.modules.unused;

import cedo.Fan;
import cedo.modules.Module;
import org.lwjgl.input.Keyboard;

public class Panic extends Module {
    boolean run;

    public Panic() {
        super("Panic", Keyboard.KEY_DELETE, Category.PLAYER);
    }

    public void onEnable() {
        run = true;
        for (Module m : Fan.modules) {
            if (run) {
                if (m.isToggled() && !m.equals(this)) {
                    m.toggle();
                }
            }
        }
        super.onEnable();
        toggle();
    }

    public void onDisable() {
        run = false;
    }
}
