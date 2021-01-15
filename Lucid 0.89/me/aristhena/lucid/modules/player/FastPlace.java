/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.aristhena.lucid.modules.player;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.client.Minecraft;

@Mod
public class FastPlace
extends Module {
    @EventTarget
    private void onUpdate(UpdateEvent event) {
        this.mc.rightClickDelayTimer = 0;
    }
}

