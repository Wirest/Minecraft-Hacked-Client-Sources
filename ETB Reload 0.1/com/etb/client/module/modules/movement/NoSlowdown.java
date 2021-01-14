package com.etb.client.module.modules.movement;

import java.awt.Color;

import com.etb.client.event.events.player.SlowdownEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.BooleanValue;
import org.greenrobot.eventbus.Subscribe;


import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


/**
 * made by oHare for ETB
 *
 * @since 4/22/2019
 **/
public class NoSlowdown extends Module {
    private BooleanValue vanilla = new BooleanValue("Vanilla", false);
    private BooleanValue items = new BooleanValue("Items", true);
    private BooleanValue sprint = new BooleanValue("Sprint", true);
    private BooleanValue water = new BooleanValue("Water", true);
    private BooleanValue soulsand = new BooleanValue("SoulSand", true);

    public NoSlowdown() {
        super("NoSlowdown", Category.MOVEMENT, new Color(102, 100, 100, 255).getRGB());
        setDescription("No slow down from blocking / eating");
        addValues(vanilla, items, sprint, water, soulsand);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (vanilla.getValue() || !mc.thePlayer.isBlocking() || !mc.thePlayer.isMoving()) return;
        if (event.isPre()) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        } else {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
        }
    }


    @Subscribe
    public void onSlowDown(SlowdownEvent event) {
        switch (event.getType()) {
            case Item:
                if (items.getValue()) event.setCanceled(true);
                break;
            case Sprinting:
                if (sprint.getValue()) event.setCanceled(true);
                break;
            case SoulSand:
                if (soulsand.getValue()) event.setCanceled(true);
                break;
            case Water:
                if (water.getValue()) event.setCanceled(true);
                break;
        }
    }
}
