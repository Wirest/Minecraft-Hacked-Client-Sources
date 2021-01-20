package cedo.modules.player;

import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.Logger;
import org.lwjgl.input.Keyboard;


public class TimerMod extends Module {

    public NumberSetting speed = new NumberSetting("Speed", 1, 0.1, 20, 0.1);
    public ModeSetting mode = new ModeSetting("Mode", "Set", "Multiplier", "Set");
    float currentSpeed = Wrapper.getTimerSpeed();


    public TimerMod() {
        super("Timer", Keyboard.KEY_NONE, Category.PLAYER);
        addSettings(speed/*, mode*/);
    }

    public void onEnable() {
        Wrapper.setTimerSpeed(1f);
        currentSpeed = Wrapper.getTimerSpeed();
    }

    public void onDisable() {
        Wrapper.setTimerSpeed(1f);
        currentSpeed = Wrapper.getTimerSpeed();
    }

    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            if (mode.is("Multiplier")) {
                Wrapper.setTimerSpeed(currentSpeed);
                currentSpeed = Wrapper.getTimerSpeed();
                Wrapper.setTimerSpeed((float) (currentSpeed * speed.getValue()));
                Logger.ingameInfo(Float.toString(Wrapper.getTimerSpeed()));
            } else if (mode.is("Set")) {
                Wrapper.setTimerSpeed(currentSpeed);
                currentSpeed = Wrapper.getTimerSpeed();
                Wrapper.setTimerSpeed((float) (speed.getValue()));
            }
        }
    }

}
