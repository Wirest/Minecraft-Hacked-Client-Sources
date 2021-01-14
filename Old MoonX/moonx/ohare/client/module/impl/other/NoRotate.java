package moonx.ohare.client.module.impl.other;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.module.Module;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

/**
 * made by oHare for oHareWare
 *
 * @since 7/19/2019
 **/
public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", Category.OTHER, new Color(0x9D9798).getRGB());
        setRenderLabel("No Rotate");
        setDescription("Cancel ncp rotation flags.");
    }
    
    @Handler
    public void handle(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            if (getMc().thePlayer != null && getMc().theWorld != null && getMc().thePlayer.rotationYaw != -180 && getMc().thePlayer.rotationPitch != 0) {
                packet.yaw = getMc().thePlayer.rotationYaw;
                packet.pitch = getMc().thePlayer.rotationPitch;
            }
        }
    }
}
