package cheatware.module.movement;

import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Keyboard.KEY_M, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
       mc.gameSettings.keyBindSprint.pressed = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
