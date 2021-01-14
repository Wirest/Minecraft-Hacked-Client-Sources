package splash.client.modules.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.network.EventPacketReceive;

/**
 * Author: Ice
 * Created: 22:24, 11-Jun-20
 * Project: Client
 */
public class NoRotate extends Module {

    public NoRotate() {
        super("NoRotate", "Cancels server side rotations.", ModuleCategory.PLAYER);
    }

    @Collect
    public void onPacketReceive(EventPacketReceive eventPacketReceive) {
        if(eventPacketReceive.getReceivedPacket() instanceof S08PacketPlayerPosLook) {
          S08PacketPlayerPosLook packetPlayerPosLook = (S08PacketPlayerPosLook) eventPacketReceive.getReceivedPacket();
          packetPlayerPosLook.setYaw(mc.thePlayer.rotationYaw);
          packetPlayerPosLook.setPitch(mc.thePlayer.rotationPitch);
        }
    }
}
