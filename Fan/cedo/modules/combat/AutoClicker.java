package cedo.modules.combat;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import cedo.util.Timer;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class AutoClicker extends Module {
    public Timer timer = new Timer();
    NumberSetting cps = new NumberSetting("CPS", 10, 1, 20, 1);

    public AutoClicker() {
        super("AutoClicker", Keyboard.KEY_NONE, Category.COMBAT);
        addSettings(cps);
    }

    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            if (mc.gameSettings.keyBindAttack.isKeyDown() && timer.hasTimeElapsed((long) (1000 / cps.getValue()), true)) {
                mc.leftClickCounter = 0;
                mc.clickMouse();
            }
        }
    }
}