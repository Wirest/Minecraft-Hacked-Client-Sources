package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import net.minecraft.network.play.client.C0BPacketEntityAction;

/**
 * Created by Arithmo on 5/1/2017 at 4:51 PM.
 */
public class KeepSprint extends Module {

    public KeepSprint(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events = EventPacket.class)
    public void onEvent(Event event) {
        EventPacket e = (EventPacket) event;
        try {
            if (e.isIncoming() && e.getPacket() instanceof C0BPacketEntityAction) {
                C0BPacketEntityAction packet = (C0BPacketEntityAction) e.getPacket();
                if (packet.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                    e.setCancelled(true);
                }
            }
        } catch (ClassCastException exception) {
        }
    }

}
