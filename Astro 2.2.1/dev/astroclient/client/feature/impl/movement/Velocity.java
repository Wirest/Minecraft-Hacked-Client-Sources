package dev.astroclient.client.feature.impl.movement;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.event.impl.packet.EventReceivePacket;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

/**
 * @author Zane2711 for PublicBase
 * @since 10/28/19
 */

@Toggleable(label = "Velocity", category = Category.MOVEMENT)
public class Velocity extends ToggleableFeature {

    @Subscribe
    public void onEvent(EventReceivePacket event) {
        if (event.getPacket() instanceof S27PacketExplosion)
            event.setCancelled();
        if (event.getPacket() instanceof S12PacketEntityVelocity)
            event.setCancelled();
    }
}
