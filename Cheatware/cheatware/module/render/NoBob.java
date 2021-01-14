package cheatware.module.render;

import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;

public class NoBob extends Module {

    public NoBob() {
        super("NoBob", Keyboard.KEY_C, Category.RENDER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    }
}
