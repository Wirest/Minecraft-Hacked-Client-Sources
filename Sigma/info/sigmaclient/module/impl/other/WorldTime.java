package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;

/**
 * Created by Arithmo on 5/6/2017 at 7:52 PM.
 */
public class WorldTime extends Module {

    public WorldTime(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events = {EventTick.class, EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventTick) {

        } else if (event instanceof EventPacket) {

        }
    }

}
