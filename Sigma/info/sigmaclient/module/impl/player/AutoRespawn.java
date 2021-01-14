package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;

public class AutoRespawn extends Module {

    public AutoRespawn(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
        if (mc.thePlayer.isDead) {
            mc.thePlayer.respawnPlayer();
        }
    }
}
