package com.etb.client.module.modules.player;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {

    public Velocity() {
        super("Velocity", Category.PLAYER, new Color(120, 120, 150, 255).getRGB());
        setDescription("Cancels velocity packets");
        setRenderlabel("Anti Velocity");
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (!event.isSending()) {
            if ((event.getPacket() instanceof S12PacketEntityVelocity) && (((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId())) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                    event.setCanceled(true);
            }
            if (event.getPacket() instanceof S27PacketExplosion) {
                S27PacketExplosion packetExplosion = (S27PacketExplosion) event.getPacket();
                    event.setCanceled(true);
            }
        }
    }
}
