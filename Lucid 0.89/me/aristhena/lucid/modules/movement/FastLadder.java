/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package me.aristhena.lucid.modules.movement;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.MoveEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

@Mod
public class FastLadder
extends Module {
    @EventTarget
    private void onMove(MoveEvent event) {
        if (event.y > 0.0 && this.mc.thePlayer.isOnLadder()) {
            event.y *= 2.4;
            //mc.thePlayer.setVelocity(0F, 0.32F, 0F);
        }
    }
}

