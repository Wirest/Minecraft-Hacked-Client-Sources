package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventLadder;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;


public class FastLadder extends Module {
    private final String MOTION = "MOTION";
    public FastLadder(ModuleData data) {
        super(data);
        settings.put(MOTION, new Setting<>(MOTION, 1, "FastLadder motion.", 0.005, 0.2, 1));
    }

    @Override
    @RegisterEvent(events = {EventLadder.class})
    public void onEvent(Event event) {
        if (event instanceof EventLadder) {
        	EventLadder el = (EventLadder)event;
        	if(el.isPre()){
        		el.setMotionY(((Number)settings.get(MOTION).getValue()).doubleValue());
        	}
        }
    }
}
