package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

    public NoRotate(ModuleData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    @Override
    @RegisterEvent(events = {EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (ep.isIncoming() && (ep.getPacket() instanceof S08PacketPlayerPosLook)) {
                S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
                pac.yaw = mc.thePlayer.rotationYaw;
                pac.pitch = mc.thePlayer.rotationPitch;
            }
        }
    }
}
