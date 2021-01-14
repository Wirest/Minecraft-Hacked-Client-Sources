package com.etb.client.module.modules.player;

import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

public class NoRotate extends Module {

    public NoRotate() {
        super("NoRotate", Category.PLAYER, new Color(154, 168, 255, 255).getRGB());
        setDescription("Cancels ncp rotation packets");
        setRenderlabel("No Rotate");
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (!mc.isSingleplayer() && mc.theWorld != null && !event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            event.setPacket(new S08PacketPlayerPosLook(packet.getX(), packet.getY(),
                    packet.getZ(), mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch,
                    packet.func_179834_f()));
        }
    }
}