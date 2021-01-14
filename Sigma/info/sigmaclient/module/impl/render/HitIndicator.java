package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import net.minecraft.network.play.server.S06PacketUpdateHealth;

/**
 * Created by Arithmo on 5/1/2017 at 8:47 PM.
 */
public class HitIndicator extends Module {

    public HitIndicator(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events = {EventRender3D.class, EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;

        }
        if (event instanceof EventRender3D) {

        }
    }

}
