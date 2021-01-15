package dev.astroclient.client.feature.impl.movement;

import dev.astroclient.client.event.impl.packet.EventReceivePacket;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

@Toggleable(label = "NoSlowdown", category = Category.MOVEMENT)
public class NoSlowdown extends ToggleableFeature {

    private boolean blocking;

    @Subscribe
    public void onEvent(EventReceivePacket e) {
        if (e.getPacket() instanceof C07PacketPlayerDigging)
            blocking = false;
        if (e.getPacket() instanceof C08PacketPlayerBlockPlacement)
            blocking = true;
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        switch (eventMotion.getEventType()) {
            case PRE:
                if (isHoldingSword() && blocking && mc.thePlayer.isBlocking()) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-.8, -.8, -.8), EnumFacing.DOWN));
                    blocking = false;
                }
                break;
            case POST:
                if (isHoldingSword() && !blocking && mc.thePlayer.isBlocking()) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-.8, -.8, -.8), 255, mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                    blocking = true;
                }
                break;
        }
    }

    private boolean isHoldingSword() {
        return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
    }
}
