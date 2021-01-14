package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;

/**
 * Created by Arithmo on 4/13/2017 at 11:29 PM.
 */
public class Timer extends Module {

    private String GS = "GAMESPEED";

    public Timer(ModuleData data) {
        super(data);
        settings.put(GS, new Setting<>(GS, 0.3, "The value the mc timer will be set to.", 0.05D, 0.1D, 5));
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }

    @Override
    @RegisterEvent(events = EventTick.class)
    public void onEvent(Event event) {
        mc.timer.timerSpeed = ((Number) settings.get(GS).getValue()).floatValue();
    }

}
