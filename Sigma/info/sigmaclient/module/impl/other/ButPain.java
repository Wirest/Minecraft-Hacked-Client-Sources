package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;

public class ButPain extends Module {
    private ButPainSub bps;

    public ButPain(ModuleData data) {
        super(data);
        EventSystem.register(this);
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate eu = (EventUpdate) event;
            if (eu.isPre()) {
                if (bps == null) {
                    bps = new ButPainSub();
                }
                bps.onUpdate();
            }
        }
    }

    @Override
    public boolean shouldRegister() {
        return false;
    }
}
