/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;

@Module.Mod(category=Module.Category.PLAYER, description="Walk around while in a bed", key=0, name="SleepWalker")
public class Ghost
extends Module {
    public boolean dead = false;

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (Wrapper.mc().currentScreen instanceof GuiSleepMP) {
            this.dead = true;
            Wrapper.getPlayer().setPositionAndUpdate(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
        }
    }

    @EventTarget
    public void onPacket(EventPacketSent event) {
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Wrapper.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.STOP_SLEEPING));
        this.dead = false;
        super.onDisable();
    }
}

