package dev.astroclient.client.feature.impl.miscellaneous;

import dev.astroclient.client.event.impl.packet.EventReceivePacket;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

@Toggleable(label = "NoRotate", category = Category.MISC)
public class NoRotate extends ToggleableFeature {

    @Subscribe
    public void onEvent(EventReceivePacket eventReceivePacket) {
        if (eventReceivePacket.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) eventReceivePacket.getPacket();
            packet.setYaw(mc.thePlayer.rotationYaw);
            packet.setPitch(mc.thePlayer.rotationPitch);
        }
    }
}
