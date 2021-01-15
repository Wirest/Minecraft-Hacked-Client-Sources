/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package me.aristhena.lucid.modules.player;

import me.aristhena.lucid.eventapi.Event;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.PotionDeincrementEvent;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

@Mod
public class PotionSaver
extends Module {
    @EventTarget
    private void onUpdate(UpdateEvent event) {
        if (event.state == Event.State.PRE && this.mc.thePlayer.motionX == 0.0 && this.mc.thePlayer.motionZ == 0.0 && this.mc.thePlayer.isCollidedVertically) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    private void onPotionDeincrement(PotionDeincrementEvent event) {
        if (this.mc.thePlayer.motionX == 0.0 && this.mc.thePlayer.motionZ == 0.0 && this.mc.thePlayer.isCollidedVertically) {
            event.setCancelled(true);
        }
    }
}

