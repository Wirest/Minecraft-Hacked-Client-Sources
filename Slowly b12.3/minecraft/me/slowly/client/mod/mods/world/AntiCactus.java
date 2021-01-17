/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;

import me.slowly.client.events.EventBlockBB;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;

public class AntiCactus
extends Mod {
    public AntiCactus() {
        super("AntiCactus", Mod.Category.WORLD, Colors.DARKGREEN.c);
    }
    @EventTarget
    private void onBoundingBox(final EventBlockBB event) {
        if (event.getBlock() instanceof BlockCactus) {
            event.setBoundingBox(new AxisAlignedBB(event.getX(), event.getY(), event.getZ(), event.getX() + 0.95, event.getBoundingBox().maxY+0.15, event.getZ() + 0.95));
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AntiCactus Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AntiCactus Enable", ClientNotification.Type.SUCCESS);
    }
}

